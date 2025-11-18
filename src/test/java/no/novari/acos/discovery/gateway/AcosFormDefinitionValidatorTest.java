package no.novari.acos.discovery.gateway;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import no.novari.acos.discovery.gateway.model.acos.AcosFormDefinition;
import no.novari.acos.discovery.gateway.model.acos.AcosFormElement;
import no.novari.acos.discovery.gateway.model.acos.AcosFormGroup;
import no.novari.acos.discovery.gateway.model.acos.AcosFormStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcosFormDefinitionValidatorTest {

    @Mock
    ValidatorFactory validatorFactory;

    @Mock
    Validator validator;

    @InjectMocks
    AcosFormDefinitionValidator acosFormDefinitionValidator;

    @BeforeEach
    void setUp() {
        when(validatorFactory.getValidator()).thenReturn(validator);
        acosFormDefinitionValidator = new AcosFormDefinitionValidator(validatorFactory);
    }

    @Test
    void shouldReturnValidatorErrors() {
        AcosFormDefinition definition = mockValidFormDefinition();

        Set<ConstraintViolation<AcosFormDefinition>> violations = new HashSet<>();
        @SuppressWarnings("unchecked")
        ConstraintViolation<AcosFormDefinition> violation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("mockedPath");
        when(violation.getPropertyPath()).thenReturn(mockPath);
        when(violation.getMessage()).thenReturn("Some validation error.");

        violations.add(violation);

        when(validator.validate(any(AcosFormDefinition.class))).thenReturn(violations);

        Optional<List<String>> result = acosFormDefinitionValidator.validate(definition);

        assertTrue(result.isPresent());
        assertTrue(result.get().contains("mockedPath Some validation error."));
    }


    @Test
    void shouldReturnEmptyWhenValidFormDefinition() {
        AcosFormDefinition definition = mockValidFormDefinition();
        when(validator.validate(any())).thenReturn(new HashSet<>());

        Optional<List<String>> result = acosFormDefinitionValidator.validate(definition);

        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnErrorsWhenInvalidFormDefinition() {
        AcosFormDefinition definition = mockFormDefinitionWithDuplicateIds();

        Optional<List<String>> result = acosFormDefinitionValidator.validate(definition);

        assertTrue(result.isPresent());
    }

    @Test
    void shouldIdentifyDuplicateElementIds() {
        AcosFormDefinition definition = mockFormDefinitionWithDuplicateIds();

        Optional<List<String>> result = acosFormDefinitionValidator.validate(definition);

        assertTrue(result.isPresent());
        assertTrue(result.get().contains("Duplicate element ID(s): [1]"));
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
