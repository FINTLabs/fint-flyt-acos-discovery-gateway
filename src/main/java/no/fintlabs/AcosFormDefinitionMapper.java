package no.fintlabs;

import no.fintlabs.model.acos.AcosFormDefinition;
import no.fintlabs.model.acos.AcosFormElement;
import no.fintlabs.model.acos.AcosFormGroup;
import no.fintlabs.model.acos.AcosFormStep;
import no.fintlabs.model.fint.InstanceElementMetadata;
import no.fintlabs.model.fint.IntegrationMetadata;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AcosFormDefinitionMapper {

    public IntegrationMetadata toIntegrationMetadata(AcosFormDefinition acosFormDefinition) {
        return IntegrationMetadata.builder()
                .id(acosFormDefinition.getMetadata().getId())
                .displayName(acosFormDefinition.getMetadata().getDisplayName())
                .instanceElementMetadata(
                        acosFormDefinition
                                .getSteps()
                                .stream()
                                .map(this::toParentElementMetadata)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private InstanceElementMetadata toParentElementMetadata(AcosFormStep acosFormStep) {
        return InstanceElementMetadata
                .parentElementMetadataBuilder()
                .id(acosFormStep.getId())
                .displayName(acosFormStep.getDisplayName())
                .children(
                        acosFormStep
                                .getGroups()
                                .stream()
                                .map(this::toParentElementMetadata)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private InstanceElementMetadata toParentElementMetadata(AcosFormGroup acosFormGroup) {
        return InstanceElementMetadata
                .parentElementMetadataBuilder()
                .id(acosFormGroup.getId())
                .displayName(acosFormGroup.getDisplayName())
                .children(
                        acosFormGroup
                                .getElements()
                                .stream()
                                .map(this::toTypedElementMetadata)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private InstanceElementMetadata toTypedElementMetadata(AcosFormElement acosFormElement) {
        return InstanceElementMetadata
                .typedElementMetadataBuilder()
                .id(acosFormElement.getId())
                .displayName(acosFormElement.getDisplayName())
                .type(acosFormElement.getType())
                .build();
    }

}
