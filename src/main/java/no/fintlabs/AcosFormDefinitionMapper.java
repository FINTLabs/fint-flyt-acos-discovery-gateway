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
                .sourceApplicationIntegrationId(acosFormDefinition.getMetadata().getFormId())
                .sourceApplicationIntegrationUri(acosFormDefinition.getMetadata().getFormUri())
                .integrationDisplayName(acosFormDefinition.getMetadata().getFormDisplayName())
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
                .builder()
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
                .builder()
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
                .builder()
                .id(acosFormElement.getId())
                .displayName(acosFormElement.getDisplayName())
                .build();
    }

}
