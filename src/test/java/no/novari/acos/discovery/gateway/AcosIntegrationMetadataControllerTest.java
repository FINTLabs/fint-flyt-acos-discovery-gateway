package no.novari.acos.discovery.gateway;

import no.novari.acos.discovery.gateway.model.acos.AcosFormDefinition;
import no.novari.acos.discovery.gateway.model.acos.AcosFormElement;
import no.novari.acos.discovery.gateway.model.acos.AcosFormStep;
import no.novari.acos.discovery.gateway.model.fint.IntegrationMetadata;
import no.novari.flyt.resourceserver.security.client.sourceapplication.SourceApplicationAuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);

        verify(acosFormDefinitionValidator).validate(acosFormDefinition);
        verify(acosFormDefinitionMapper).toIntegrationMetadata(eq(sourceApplicationid), eq(acosFormDefinition));
        verify(integrationMetadataProducerService).publishNewIntegrationMetadata(mockedMetadata);
    }

    @Test
    void postIntegrationMetadata_ValidationErrors_ShouldReturnUnprocessableEntity() {
        AcosFormDefinition acosFormDefinition = mockFormDefinitionWithDuplicateIds();

        when(acosFormDefinitionValidator.validate(acosFormDefinition)).thenReturn(Optional.of(Collections.singletonList("Error")));

        Mono<ResponseEntity<?>> result = controller.postIntegrationMetadata(acosFormDefinition, Mono.just(authentication));

        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof ResponseStatusException
                        && ((ResponseStatusException) throwable).getStatusCode() == HttpStatus.UNPROCESSABLE_ENTITY)
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
        AcosFormElement group = AcosFormElement
                .builder()
                .displayName("TestGroup")
                .type("Group")
                .elements(Arrays.asList(element1, element2))
                .build();
        AcosFormStep step = AcosFormStep
                .builder()
                .displayName("TestStep")
                .elements(List.of(group))
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
        AcosFormElement group = AcosFormElement
                .builder()
                .displayName("TestGroup")
                .type("Group")
                .elements(Arrays.asList(element1, element2))
                .build();
        AcosFormStep step = AcosFormStep
                .builder()
                .displayName("TestStep")
                .elements(List.of(group))
                .build();
        return new AcosFormDefinition(null, List.of(step));
    }

}
