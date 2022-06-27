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
                                .id("Test0488")
                                .displayName("Test integration")
                                .build()
                )
                .steps(asList(
                        AcosFormStep
                                .builder()
                                .id("person_med_valg")
                                .displayName("Person med valg")
                                .groups(asList(
                                        AcosFormGroup
                                                .builder()
                                                .id("person_1")
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
                                                .id("person_2")
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
                .id("Test0488")
                .displayName("Test integration")
                .instanceElementMetadata(asList(
                        InstanceElementMetadata
                                .builder()
                                .id("person_med_valg")
                                .displayName("Person med valg")
                                .children(asList(
                                        InstanceElementMetadata
                                                .builder()
                                                .id("person_1")
                                                .displayName("Person 1")
                                                .children(asList(
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .id("person_1.fornavn")
                                                                .displayName("Fornavn")
                                                                .build(),
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .id("person_1.etternavn")
                                                                .displayName("Etternavn")
                                                                .build()
                                                ))
                                                .build(),
                                        InstanceElementMetadata
                                                .builder()
                                                .id("person_2")
                                                .displayName("Person 2")
                                                .children(asList(
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .id("person_2.fornavn")
                                                                .displayName("Fornavn")
                                                                .build(),
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .id("person_2.etternavn")
                                                                .displayName("Etternavn")
                                                                .build(),
                                                        InstanceElementMetadata
                                                                .builder()
                                                                .id("person_2.fødselsdato")
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
        IntegrationMetadata mappingResult = acosFormDefinitionMapper.toIntegrationMetadata(acosFormDefinition)

        then:
        mappingResult == expectedIntegrationMetadata
    }
}
