package no.fintlabs;

import no.fintlabs.model.acos.AcosFormDefinition;
import no.fintlabs.model.acos.AcosFormElement;
import no.fintlabs.model.acos.AcosFormGroup;
import no.fintlabs.model.acos.AcosFormStep;
import no.fintlabs.model.fint.InstanceElementMetadata;
import no.fintlabs.model.fint.IntegrationMetadata;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AcosFormDefinitionMapper {

    public IntegrationMetadata toIntegrationMetadata(Long sourceApplicationId, AcosFormDefinition acosFormDefinition) {

        return IntegrationMetadata.builder()
                .sourceApplicationId(sourceApplicationId)
                .sourceApplicationIntegrationId(acosFormDefinition.getMetadata().getFormId())
                .sourceApplicationIntegrationUri(acosFormDefinition.getMetadata().getFormUri())
                .integrationDisplayName(acosFormDefinition.getMetadata().getFormDisplayName())
                .version(acosFormDefinition.getMetadata().getVersion())
                .instanceElementMetadata(
                        Stream.concat(
                                        Stream.of(
                                                createSkjemaPdfMetadata(),
                                                createVedleggMetadata()
                                        ),
                                        acosFormDefinition
                                                .getSteps()
                                                .stream()
                                                .map(this::toParentElementMetadata)
                                )
                                .toList()
                )
                .build();
    }

    private InstanceElementMetadata createSkjemaPdfMetadata() {
        return InstanceElementMetadata
                .builder()
                .displayName("Skjema-PDF")
                .key("skjemaPdf")
                .type(InstanceElementMetadata.Type.STRING)
                .disabled(true)
                .build();
    }

    private InstanceElementMetadata createVedleggMetadata() {
        return InstanceElementMetadata
                .builder()
                .displayName("Vedlegg")
                .key("vedlegg")
                .disabled(true)
                .children(List.of(
                        InstanceElementMetadata
                                .builder()
                                .displayName("Navn")
                                .key("navn")
                                .type(InstanceElementMetadata.Type.STRING)
                                .disabled(true)
                                .build(),
                        InstanceElementMetadata
                                .builder()
                                .displayName("Type")
                                .key("type")
                                .type(InstanceElementMetadata.Type.STRING)
                                .disabled(true)
                                .build(),
                        InstanceElementMetadata
                                .builder()
                                .displayName("Enkoding")
                                .key("enkoding")
                                .type(InstanceElementMetadata.Type.STRING)
                                .disabled(true)
                                .build(),
                        InstanceElementMetadata
                                .builder()
                                .displayName("Fil")
                                .key("fil")
                                .type(InstanceElementMetadata.Type.STRING)
                                .disabled(true)
                                .build()
                ))
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
                .key("skjema." + acosFormElement.getId())
                .type(InstanceElementMetadata.Type.STRING)
                .displayName(acosFormElement.getDisplayName())
                .build();
    }

}
