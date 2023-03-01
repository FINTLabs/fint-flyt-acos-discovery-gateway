package no.fintlabs;

import no.fintlabs.model.acos.AcosFormDefinition;
import no.fintlabs.model.acos.AcosFormElement;
import no.fintlabs.model.acos.AcosFormGroup;
import no.fintlabs.model.acos.AcosFormStep;
import no.fintlabs.model.fint.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcosFormDefinitionMapper {

    public IntegrationMetadata toIntegrationMetadata(Long sourceApplicationId, AcosFormDefinition acosFormDefinition) {
        return IntegrationMetadata.builder()
                .sourceApplicationId(sourceApplicationId)
                .sourceApplicationIntegrationId(acosFormDefinition.getMetadata().getFormId())
                .sourceApplicationIntegrationUri(acosFormDefinition.getMetadata().getFormUri())
                .integrationDisplayName(acosFormDefinition.getMetadata().getFormDisplayName())
                .version(acosFormDefinition.getMetadata().getVersion())
                .instanceMetadata(
                        InstanceMetadataContent.builder()
                                .instanceValueMetadata(List.of(createSkjemaPdfMetadata()))
                                .instanceObjectCollectionMetadata(List.of(createVedleggMetadata()))
                                .categories(
                                        acosFormDefinition
                                                .getSteps()
                                                .stream()
                                                .map(this::toMetadataCategory)
                                                .toList()
                                )
                                .build()
                )
                .build();
    }

    private InstanceValueMetadata createSkjemaPdfMetadata() {
        return InstanceValueMetadata
                .builder()
                .displayName("Skjema-PDF")
                .key("skjemaPdf")
                .type(InstanceValueMetadata.Type.FILE)
                .build();
    }

    private InstanceObjectCollectionMetadata createVedleggMetadata() {
        return InstanceObjectCollectionMetadata
                .builder()
                .displayName("Vedlegg")
                .key("vedlegg")
                .objectMetadata(
                        InstanceMetadataContent
                                .builder()
                                .instanceValueMetadata(List.of(
                                        InstanceValueMetadata
                                                .builder()
                                                .displayName("Navn")
                                                .key("navn")
                                                .type(InstanceValueMetadata.Type.STRING)
                                                .build(),
                                        InstanceValueMetadata
                                                .builder()
                                                .displayName("Type")
                                                .key("type")
                                                .type(InstanceValueMetadata.Type.STRING)
                                                .build(),
                                        InstanceValueMetadata
                                                .builder()
                                                .displayName("Enkoding")
                                                .key("enkoding")
                                                .type(InstanceValueMetadata.Type.STRING)
                                                .build(),
                                        InstanceValueMetadata
                                                .builder()
                                                .displayName("Fil")
                                                .key("fil")
                                                .type(InstanceValueMetadata.Type.FILE)
                                                .build()
                                ))
                                .build()
                )
                .build();
    }

    private InstanceMetadataCategory toMetadataCategory(AcosFormStep acosFormStep) {
        return InstanceMetadataCategory
                .builder()
                .displayName(acosFormStep.getDisplayName())
                .content(
                        InstanceMetadataContent
                                .builder()
                                .categories(
                                        acosFormStep
                                                .getGroups()
                                                .stream()
                                                .map(this::toMetadataCategory)
                                                .toList()
                                )
                                .build()
                )
                .build();
    }

    private InstanceMetadataCategory toMetadataCategory(AcosFormGroup acosFormGroup) {
        return InstanceMetadataCategory
                .builder()
                .displayName(acosFormGroup.getDisplayName())
                .content(
                        InstanceMetadataContent
                                .builder()
                                .instanceValueMetadata(
                                        acosFormGroup
                                                .getElements()
                                                .stream()
                                                .map(this::toInstanceValueMetadata)
                                                .toList()
                                )
                                .build()
                )
                .build();
    }

    private InstanceValueMetadata toInstanceValueMetadata(AcosFormElement acosFormElement) {
        return InstanceValueMetadata
                .builder()
                .displayName(acosFormElement.getDisplayName())
                .type(InstanceValueMetadata.Type.STRING)
                .key("skjema." + acosFormElement.getId())
                .build();
    }

}
