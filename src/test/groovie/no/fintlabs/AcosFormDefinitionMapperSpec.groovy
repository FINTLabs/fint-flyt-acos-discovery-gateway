package no.fintlabs

import no.fintlabs.model.acos.*
import no.fintlabs.model.fint.InstanceElementMetadata
import no.fintlabs.model.fint.IntegrationMetadata
import spock.lang.Specification

import static java.util.Arrays.asList

class AcosFormDefinitionMapperSpec extends Specification {

    private AcosFormDefinitionMapper acosFormDefinitionMapper
    private AcosFormDefinition acosFormDefinition
    private IntegrationMetadata expectedIntegrationMetadata

    def setup() {
        acosFormDefinitionMapper = new AcosFormDefinitionMapper()

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
                .steps(asList(
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
                .build()

        expectedIntegrationMetadata = IntegrationMetadata
                .builder()
                .sourceApplicationId(1)
                .sourceApplicationIntegrationId("Test0488")
                .integrationDisplayName("Test integration")
                .sourceApplicationIntegrationUri("https://edit.acos.com?formid=test0488")
                .instanceElementMetadata(asList(
                        InstanceElementMetadata
                                .builder()
                                .displayName("Skjema-PDF")
                                .key("skjemaPdf")
                                .type(InstanceElementMetadata.Type.STRING)
                                .disabled(true)
                                .build(),
                        InstanceElementMetadata
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
                                                .build(),
                                ))
                                .build(),
                        InstanceElementMetadata
                                .builder()
                                .displayName("Person med valg")
                                .children(asList(
                                        InstanceElementMetadata
                                                .builder()
                                                .displayName("Person 1")
                                                .children(asList(
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .key("skjema.person_1.fornavn")
                                                                .type(InstanceElementMetadata.Type.STRING)
                                                                .displayName("Fornavn")
                                                                .build(),
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .key("skjema.person_1.etternavn")
                                                                .type(InstanceElementMetadata.Type.STRING)
                                                                .displayName("Etternavn")
                                                                .build()
                                                ))
                                                .build(),
                                        InstanceElementMetadata
                                                .builder()
                                                .displayName("Person 2")
                                                .children(asList(
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .key("skjema.person_2.fornavn")
                                                                .type(InstanceElementMetadata.Type.STRING)
                                                                .displayName("Fornavn")
                                                                .build(),
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .key("skjema.person_2.etternavn")
                                                                .type(InstanceElementMetadata.Type.STRING)
                                                                .displayName("Etternavn")
                                                                .build(),
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .key("skjema.person_2.fødselsdato")
                                                                .type(InstanceElementMetadata.Type.STRING)
                                                                .displayName("Fødselsdato")
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build()
                ))
                .build()
    }

    def 'should map to IntegrationMetadata'() {
        when:
        IntegrationMetadata mappingResult = acosFormDefinitionMapper.toIntegrationMetadata(1, acosFormDefinition)

        then:
        mappingResult == expectedIntegrationMetadata
    }
}
