package no.novari.acos.discovery.gateway;

import no.novari.acos.discovery.gateway.model.acos.AcosFormDefinition;
import no.novari.acos.discovery.gateway.model.acos.AcosFormElement;
import no.novari.acos.discovery.gateway.model.acos.AcosFormStep;
import no.novari.acos.discovery.gateway.model.fint.InstanceMetadataCategory;
import no.novari.acos.discovery.gateway.model.fint.InstanceMetadataContent;
import no.novari.acos.discovery.gateway.model.fint.InstanceObjectCollectionMetadata;
import no.novari.acos.discovery.gateway.model.fint.InstanceValueMetadata;
import no.novari.acos.discovery.gateway.model.fint.IntegrationMetadata;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcosFormDefinitionMapper {

    private static final String GROUP_TYPE = "Group";

    public IntegrationMetadata toIntegrationMetadata(Long sourceApplicationId, AcosFormDefinition acosFormDefinition) {
        return IntegrationMetadata
                .builder()
                .sourceApplicationId(sourceApplicationId)
                .sourceApplicationIntegrationId(acosFormDefinition.getMetadata().getFormId())
                .sourceApplicationIntegrationUri(acosFormDefinition.getMetadata().getFormUri())
                .integrationDisplayName(acosFormDefinition.getMetadata().getFormDisplayName())
                .version(acosFormDefinition.getMetadata().getVersion())
                .instanceMetadata(
                        InstanceMetadataContent
                                .builder()
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
                .content(toMetadataContent(acosFormStep.getElements()))
                .build();
    }

    private InstanceMetadataCategory toMetadataCategory(AcosFormElement acosFormElement) {
        return InstanceMetadataCategory
                .builder()
                .displayName(acosFormElement.getDisplayName())
                .content(toMetadataContent(acosFormElement.getElements()))
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

    private InstanceMetadataContent toMetadataContent(List<AcosFormElement> elements) {
        List<AcosFormElement> safeElements = elements == null ? List.of() : elements;

        List<InstanceValueMetadata> valueMetadata = safeElements
                .stream()
                .filter(this::isValueElement)
                .map(this::toInstanceValueMetadata)
                .toList();

        List<InstanceMetadataCategory> categories = safeElements
                .stream()
                .filter(this::isGroupElement)
                .map(this::toMetadataCategory)
                .toList();

        return InstanceMetadataContent
                .builder()
                .instanceValueMetadata(valueMetadata)
                .categories(categories)
                .build();
    }

    private boolean isGroupElement(AcosFormElement element) {
        return element != null && GROUP_TYPE.equalsIgnoreCase(element.getType());
    }

    private boolean isValueElement(AcosFormElement element) {
        return element != null && !isGroupElement(element) && hasText(element.getId());
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

}
