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

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AcosFormDefinitionValidatorTest {

    @Mock
    ValidatorFactory validatorFactory;

    @Mock
    Validator validator;

    @InjectMocks
    AcosFormDefinitionValidator acosFormDefinitionValidator;

//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        when(validatorFactory.getValidator()).thenReturn(validator);
//    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // or openMocks, depending on Mockito version
        when(validatorFactory.getValidator()).thenReturn(validator);
        acosFormDefinitionValidator = new AcosFormDefinitionValidator(validatorFactory);
    }

    @Test
    void shouldReturnEmptyWhenValidFormDefinition() {
        AcosFormDefinition definition = mockValidFormDefinition();
        when(validator.validate(any())).thenReturn(new HashSet<>()); // assuming no validation errors

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
