package no.novari.acos.discovery.gateway;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import no.novari.acos.discovery.gateway.model.acos.AcosFormDefinition;
import no.novari.acos.discovery.gateway.model.acos.AcosFormElement;
import no.novari.acos.discovery.gateway.model.acos.AcosFormGroup;
import no.novari.acos.discovery.gateway.model.acos.AcosFormStep;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@Slf4j
public class AcosFormDefinitionValidator {

    private final Validator fieldValidator;

    public AcosFormDefinitionValidator(ValidatorFactory validatorFactory) {
        this.fieldValidator = validatorFactory.getValidator();
    }

    public Optional<List<String>> validate(AcosFormDefinition acosFormDefinition) {
        List<String> errors = fieldValidator.validate(acosFormDefinition)
                .stream()
                .map(constraintViolation ->
                        constraintViolation.getPropertyPath() + " " + constraintViolation.getMessage())
                .sorted()
                .collect(Collectors.toList());

        validateElementIds(acosFormDefinition).ifPresent(errors::add);

        return errors.isEmpty()
                ? Optional.empty()
                : Optional.of(errors);
    }

    private Optional<String> validateElementIds(AcosFormDefinition acosFormDefinition) {
        List<String> duplicateElementIds = findDuplicateElementIds(getElements(acosFormDefinition));
        return duplicateElementIds.isEmpty()
                ? Optional.empty()
                : Optional.of("Duplicate element ID(s): " + duplicateElementIds);
    }

    private List<AcosFormElement> getElements(AcosFormDefinition acosFormDefinition) {
        return Optional.ofNullable(acosFormDefinition.getSteps()).orElse(emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(AcosFormStep::getGroups)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(AcosFormGroup::getElements)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<String> findDuplicateElementIds(List<AcosFormElement> acosFormElements) {
        Set<String> items = new HashSet<>();
        return acosFormElements.stream()
                .map(AcosFormElement::getId)
                .filter(Objects::nonNull)
                .filter(n -> !items.add(n))
                .collect(Collectors.toList());
    }

}
