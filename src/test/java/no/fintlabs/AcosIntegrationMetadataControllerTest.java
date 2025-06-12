package no.fintlabs;

import no.fintlabs.model.acos.*;
import no.fintlabs.model.fint.*;
import no.fintlabs.resourceserver.security.client.sourceapplication.SourceApplicationAuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AcosIntegrationMetadataControllerTest {

    @Mock
    private AcosFormDefinitionMapper acosFormDefinitionMapper;
    @Mock
    AcosFormDefinitionValidator acosFormDefinitionValidator;
    @Mock
    private IntegrationMetadataProducerService integrationMetadataProducerService;
    @Mock
    private Authentication authentication;
    @Mock
    private SourceApplicationAuthorizationService sourceApplicationAuthorizationService;
    @InjectMocks
    private AcosIntegrationMetadataController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void postIntegrationMetadata_ValidInput_ShouldReturnAccepted() {
        long sourceApplicationid = 1L;

        IntegrationMetadata mockedMetadata = mock(IntegrationMetadata.class);
        AcosFormDefinition acosFormDefinition = mockValidFormDefinition();

        when(sourceApplicationAuthorizationService.getSourceApplicationId(authentication)).thenReturn(sourceApplicationid);
        when(acosFormDefinitionValidator.validate(acosFormDefinition)).thenReturn(Optional.empty());  // No errors
        when(acosFormDefinitionMapper.toIntegrationMetadata(eq(sourceApplicationid), eq(acosFormDefinition))).thenReturn(mockedMetadata);
        doNothing().when(integrationMetadataProducerService).publishNewIntegrationMetadata(mockedMetadata);

        ResponseEntity<?> responseEntity = controller.processIntegrationMetadata(acosFormDefinition, authentication);

        assertEquals(202, responseEntity.getStatusCodeValue());

        verify(acosFormDefinitionValidator, times(1)).validate(acosFormDefinition);
        verify(acosFormDefinitionMapper, times(1)).toIntegrationMetadata(eq(sourceApplicationid), eq(acosFormDefinition));
        verify(integrationMetadataProducerService, times(1)).publishNewIntegrationMetadata(mockedMetadata);
    }

    @Test
    void postIntegrationMetadata_ValidationErrors_ShouldReturnUnprocessableEntity() {
        AcosFormDefinition acosFormDefinition = mockFormDefinitionWithDuplicateIds();

        when(acosFormDefinitionValidator.validate(acosFormDefinition)).thenReturn(Optional.of(Collections.singletonList("Error")));

        Mono<ResponseEntity<?>> result = controller.postIntegrationMetadata(acosFormDefinition, Mono.just(authentication));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException
                        && ((ResponseStatusException) throwable).getStatus() == HttpStatus.UNPROCESSABLE_ENTITY)
                .verify();

        verify(integrationMetadataProducerService, never()).publishNewIntegrationMetadata(any());
    }

    @SuppressWarnings("ReactiveStreamsUnusedPublisher")
    @Test
    void shouldThrowResponseStatusExceptionWhenInvalid() {
        AcosFormDefinition mockFormDefinition = mockFormDefinitionWithDuplicateIds();

        List<String> validationErrors = List.of("Error1", "Error2");

        when(acosFormDefinitionValidator.validate(mockFormDefinition)).thenReturn(Optional.of(validationErrors));

        Mono<ResponseEntity<?>> resultMono = controller.postIntegrationMetadata(mockFormDefinition, Mono.just(authentication));

        assertThrows(ResponseStatusException.class, resultMono::block);
    }


    private AcosFormDefinition mockValidFormDefinition() {
        AcosFormElement element1 = AcosFormElement
                .builder()
                .id("1")
                .displayName("TestElement1")
                .type("Type1")
                .build();
        AcosFormElement element2 = AcosFormElement
                .builder()
                .id("2")
                .displayName("TestElement2")
                .type("Type2")
                .build();
        AcosFormGroup group = AcosFormGroup
                .builder()
                .displayName("TestGroup")
                .elements(Arrays.asList(element1, element2))
                .build();
        AcosFormStep step = AcosFormStep
                .builder()
                .displayName("TestStep")
                .groups(List.of(group))
                .build();
        return new AcosFormDefinition(null, List.of(step));
    }

    private AcosFormDefinition mockFormDefinitionWithDuplicateIds() {
        AcosFormElement element1 = AcosFormElement
                .builder()
                .id("1")
                .displayName("TestElement1")
                .type("Type1")
                .build();
        AcosFormElement element2 = AcosFormElement
                .builder()
                .id("1")
                .displayName("TestElement2")
                .type("Type2")
                .build();
        AcosFormGroup group = AcosFormGroup
                .builder()
                .displayName("TestGroup")
                .elements(Arrays.asList(element1, element2))
                .build();
        AcosFormStep step = AcosFormStep
                .builder()
                .displayName("TestStep")
                .groups(List.of(group))
                .build();
        return new AcosFormDefinition(null, List.of(step));
    }

}
