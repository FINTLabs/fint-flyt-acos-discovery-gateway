package no.novari.acos.discovery.gateway;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.extern.slf4j.Slf4j;
import no.novari.acos.discovery.gateway.model.acos.AcosFormDefinition;
import no.novari.acos.discovery.gateway.model.acos.AcosFormElement;
import no.novari.acos.discovery.gateway.model.acos.AcosFormStep;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        validateElementIds(acosFormDefinition).ifPresent(errors::addAll);

        return errors.isEmpty()
                ? Optional.empty()
                : Optional.of(errors);
    }

    private Optional<List<String>> validateElementIds(AcosFormDefinition acosFormDefinition) {
        List<AcosFormElement> elements = getElements(acosFormDefinition);

        List<String> errors = new ArrayList<>();

        List<String> missingElementIds = findMissingElementIds(elements);
        if (!missingElementIds.isEmpty()) {
            errors.add("Missing element ID(s) for: " + missingElementIds);
        }

        List<String> duplicateElementIds = findDuplicateElementIds(elements);
        if (!duplicateElementIds.isEmpty()) {
            errors.add("Duplicate element ID(s): " + duplicateElementIds);
        }

        return errors.isEmpty() ? Optional.empty() : Optional.of(errors);
    }

    private List<AcosFormElement> getElements(AcosFormDefinition acosFormDefinition) {
        return Optional.ofNullable(acosFormDefinition.getSteps()).orElse(emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(AcosFormStep::getElements)
                .filter(Objects::nonNull)
                .flatMap(elements -> flattenElements(elements).stream())
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

    private List<String> findMissingElementIds(List<AcosFormElement> acosFormElements) {
        return acosFormElements
                .stream()
                .filter(Objects::nonNull)
                .filter(element -> !isGroupElement(element))
                .filter(element -> element.getId() == null || element.getId().isBlank())
                .map(AcosFormElement::getDisplayName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean isGroupElement(AcosFormElement element) {
        return element.getType() != null && "Group".equalsIgnoreCase(element.getType());
    }

    private List<AcosFormElement> flattenElements(Collection<AcosFormElement> elements) {
        List<AcosFormElement> flattened = new ArrayList<>();
        for (AcosFormElement element : elements) {
            if (element == null) {
                continue;
            }
            flattened.add(element);
            List<AcosFormElement> children = element.getElements();
            if (children != null && !children.isEmpty()) {
                flattened.addAll(flattenElements(children));
            }
        }
        return flattened;
    }

}
