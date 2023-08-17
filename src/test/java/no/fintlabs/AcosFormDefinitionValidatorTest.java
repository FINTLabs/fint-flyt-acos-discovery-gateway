package no.fintlabs;

import no.fintlabs.model.acos.AcosFormDefinition;
import no.fintlabs.model.acos.AcosFormElement;
import no.fintlabs.model.acos.AcosFormGroup;
import no.fintlabs.model.acos.AcosFormStep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AcosFormDefinitionValidatorTest {

    @Mock
    ValidatorFactory validatorFactory;

    @Mock
    Validator validator;

    @InjectMocks
    AcosFormDefinitionValidator acosFormDefinitionValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
