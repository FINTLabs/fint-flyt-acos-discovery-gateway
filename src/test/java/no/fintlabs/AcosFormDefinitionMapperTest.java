package no.fintlabs;

import no.fintlabs.model.acos.*;
import no.fintlabs.model.fint.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AcosFormDefinitionMapperTest {

    private AcosFormDefinitionMapper acosFormDefinitionMapper;
    private AcosFormDefinition acosFormDefinition;
    private IntegrationMetadata expectedIntegrationMetadata;

    @BeforeEach
    void setup() {
        acosFormDefinitionMapper = new AcosFormDefinitionMapper();

        acosFormDefinition = AcosFormDefinition
                .builder()
                .metadata(
                        AcosFormMetadata
                                .builder()
                                .formId("Test0488")
                                .formDisplayName("Test integration")
                                .formUri("https://edit.acos.com?formid=test0488")
                                .build()
                )
                .steps(Collections.singletonList(
                        AcosFormStep
                                .builder()
                                .displayName("Person med valg")
                                .groups(asList(
                                        AcosFormGroup
                                                .builder()
                                                .displayName("Person 1")
                                                .elements(asList(
                                                        AcosFormElement
                                                                .builder()
                                                                .id("person_1.fornavn")
                                                                .displayName("Fornavn")
                                                                .type("InputBox")
                                                                .build(),
                                                        AcosFormElement
                                                                .builder()
                                                                .id("person_1.etternavn")
                                                                .displayName("Etternavn")
                                                                .type("InputBox")
                                                                .build()
                                                ))
                                                .build(),
                                        AcosFormGroup
                                                .builder()
                                                .displayName("Person 2")
                                                .elements(asList(
                                                        AcosFormElement
                                                                .builder()
                                                                .id("person_2.fornavn")
                                                                .displayName("Fornavn")
                                                                .type("InputBox")
                                                                .build(),
                                                        AcosFormElement
                                                                .builder()
                                                                .id("person_2.etternavn")
                                                                .displayName("Etternavn")
                                                                .type("InputBox")
                                                                .build(),
                                                        AcosFormElement
                                                                .builder()
                                                                .id("person_2.fødselsdato")
                                                                .displayName("Fødselsdato")
                                                                .type("NumberBox")
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build()
                ))
                .build();

        expectedIntegrationMetadata = IntegrationMetadata
                .builder()
                .sourceApplicationId(1L)
                .sourceApplicationIntegrationId("Test0488")
                .integrationDisplayName("Test integration")
                .sourceApplicationIntegrationUri("https://edit.acos.com?formid=test0488")
                .instanceMetadata(
                        InstanceMetadataContent
                                .builder()
                                .instanceValueMetadata(List.of(
                                        InstanceValueMetadata
                                                .builder()
                                                .displayName("Skjema-PDF")
                                                .key("skjemaPdf")
                                                .type(InstanceValueMetadata.Type.FILE)
                                                .build()
                                ))
                                .instanceObjectCollectionMetadata(List.of(
                                        InstanceObjectCollectionMetadata
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
                                                                .instanceObjectCollectionMetadata(List.of())
                                                                .categories(List.of())
                                                                .build()
                                                )
                                                .build()
                                ))
                                .categories(List.of(
                                        InstanceMetadataCategory
                                                .builder()
                                                .displayName("Person med valg")
                                                .content(
                                                        InstanceMetadataContent
                                                                .builder()
                                                                .categories(List.of(
                                                                        InstanceMetadataCategory
                                                                                .builder()
                                                                                .displayName("Person 1")
                                                                                .content(
                                                                                        InstanceMetadataContent
                                                                                                .builder()
                                                                                                .instanceValueMetadata(
                                                                                                        List.of(
                                                                                                                InstanceValueMetadata
                                                                                                                        .builder()
                                                                                                                        .key("skjema.person_1.fornavn")
                                                                                                                        .type(InstanceValueMetadata.Type.STRING)
                                                                                                                        .displayName("Fornavn")
                                                                                                                        .build(),
                                                                                                                InstanceValueMetadata
                                                                                                                        .builder()
                                                                                                                        .key("skjema.person_1.etternavn")
                                                                                                                        .type(InstanceValueMetadata.Type.STRING)
                                                                                                                        .displayName("Etternavn")
                                                                                                                        .build()
                                                                                                        )
                                                                                                )
                                                                                                .instanceObjectCollectionMetadata(List.of())
                                                                                                .categories(List.of())
                                                                                                .build()
                                                                                )
                                                                                .build(),
                                                                        InstanceMetadataCategory
                                                                                .builder()
                                                                                .displayName("Person 2")
                                                                                .content(
                                                                                        InstanceMetadataContent
                                                                                                .builder()
                                                                                                .instanceValueMetadata(List.of(
                                                                                                        InstanceValueMetadata
                                                                                                                .builder()
                                                                                                                .key("skjema.person_2.fornavn")
                                                                                                                .type(InstanceValueMetadata.Type.STRING)
                                                                                                                .displayName("Fornavn")
                                                                                                                .build(),
                                                                                                        InstanceValueMetadata
                                                                                                                .builder()
                                                                                                                .key("skjema.person_2.etternavn")
                                                                                                                .type(InstanceValueMetadata.Type.STRING)
                                                                                                                .displayName("Etternavn")
                                                                                                                .build(),
                                                                                                        InstanceValueMetadata
                                                                                                                .builder()
                                                                                                                .key("skjema.person_2.fødselsdato")
                                                                                                                .type(InstanceValueMetadata.Type.STRING)
                                                                                                                .displayName("Fødselsdato")
                                                                                                                .build()
                                                                                                ))
                                                                                                .instanceObjectCollectionMetadata(List.of())
                                                                                                .categories(List.of())
                                                                                                .build()
                                                                                )
                                                                                .build()
                                                                ))
                                                                .build()
                                                )
                                                .build()
                                ))
                                .build()
                )
                .build();
    }

    @Test
    void shouldMapToIntegrationMetadata() {
        IntegrationMetadata mappingResult = acosFormDefinitionMapper.toIntegrationMetadata(1L, acosFormDefinition);

        assertEquals(expectedIntegrationMetadata, mappingResult);
    }

}
