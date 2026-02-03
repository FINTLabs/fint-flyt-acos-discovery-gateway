package no.novari.acos.discovery.gateway;

import no.novari.acos.discovery.gateway.model.acos.AcosFormDefinition;
import no.novari.acos.discovery.gateway.model.acos.AcosFormElement;
import no.novari.acos.discovery.gateway.model.acos.AcosFormMetadata;
import no.novari.acos.discovery.gateway.model.acos.AcosFormSavedValues;
import no.novari.acos.discovery.gateway.model.acos.AcosFormStep;
import no.novari.acos.discovery.gateway.model.fint.InstanceMetadataCategory;
import no.novari.acos.discovery.gateway.model.fint.InstanceMetadataContent;
import no.novari.acos.discovery.gateway.model.fint.InstanceObjectCollectionMetadata;
import no.novari.acos.discovery.gateway.model.fint.InstanceValueMetadata;
import no.novari.acos.discovery.gateway.model.fint.IntegrationMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

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
                .savedValues(
                        AcosFormSavedValues
                                .builder()
                                .displayName("SavedValues")
                                .elements(List.of(
                                        AcosFormElement
                                                .builder()
                                                .displayName("From logic")
                                                .type("Group")
                                                .elements(List.of(
                                                        AcosFormElement
                                                                .builder()
                                                                .id("saved.logic")
                                                                .displayName("Logic field")
                                                                .type("InputBox")
                                                                .build()
                                                ))
                                                .build()
                                ))
                                .build()
                )
                .steps(Collections.singletonList(
                        AcosFormStep
                                .builder()
                                .displayName("Person med valg")
                                .elements(asList(
                                        AcosFormElement
                                                .builder()
                                                .displayName("Person 1")
                                                .type("Group")
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
                                        AcosFormElement
                                                .builder()
                                                .displayName("Person 2")
                                                .type("Group")
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
                                                .displayName("SavedValues")
                                                .content(
                                                        InstanceMetadataContent
                                                                .builder()
                                                                .instanceValueMetadata(List.of())
                                                                .instanceObjectCollectionMetadata(List.of())
                                                                .categories(List.of(
                                                                        InstanceMetadataCategory
                                                                                .builder()
                                                                                .displayName("From logic")
                                                                                .content(
                                                                                        InstanceMetadataContent
                                                                                                .builder()
                                                                                                .instanceValueMetadata(
                                                                                                        List.of(
                                                                                                                InstanceValueMetadata
                                                                                                                        .builder()
                                                                                                                        .key("skjema.saved.logic")
                                                                                                                        .type(InstanceValueMetadata.Type.STRING)
                                                                                                                        .displayName("Logic field")
                                                                                                                        .build()
                                                                                                        )
                                                                                                )
                                                                                                .instanceObjectCollectionMetadata(List.of())
                                                                                                .categories(List.of())
                                                                                                .build()
                                                                                )
                                                                                .build()
                                                                ))
                                                                .build()
                                                )
                                                .build(),
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

        assertThat(mappingResult).isEqualTo(expectedIntegrationMetadata);
    }

    @Test
    void shouldMapFullFormDefinitionWithSavedValues() {
        AcosFormDefinition definition = AcosFormDefinition
                .builder()
                .metadata(
                        AcosFormMetadata
                                .builder()
                                .formId("ACOS-11")
                                .formDisplayName("FINT-test")
                                .formUri("")
                                .version(5L)
                                .build()
                )
                .savedValues(
                        AcosFormSavedValues
                                .builder()
                                .displayName("SavedValues")
                                .elements(List.of(
                                        group("From logic", List.of()),
                                        group("From logged in user", List.of(
                                                input("Login.UserID", "User Id"),
                                                input("Login.FirstName", "First name"),
                                                input("Login.LastName", "Last name"),
                                                input("Login.Address", "Address"),
                                                input("Login.PostalCode", "Postal code"),
                                                input("Login.PostalArea", "Postal area"),
                                                input("Login.Telephone", "Telephone"),
                                                input("Login.Email", "Email address"),
                                                group("ID Porten", List.of(
                                                        group("Folkeregister", List.of(
                                                                input("Login.IDPorten.Folkeregister.Fødselsnummer", "National ID-number"),
                                                                input("Login.IDPorten.Folkeregister.Gatenavn", "Street name"),
                                                                input("Login.IDPorten.Folkeregister.Gatenummer", "Street number"),
                                                                input("Login.IDPorten.Folkeregister.Husbokstav", "House letter"),
                                                                input("Login.IDPorten.Folkeregister.Postnummer", "Postal code"),
                                                                input("Login.IDPorten.Folkeregister.Poststed", "Postal area")
                                                        )),
                                                        group("KRR", List.of(
                                                                input("Login.IDPorten.KRR.Telefon", "Telephone"),
                                                                input("Login.IDPorten.KRR.Epost", "Email"),
                                                                input("Login.IDPorten.KRR.Reservert", "Reserved")
                                                        ))
                                                )),
                                                group("Entra ID", List.of(
                                                        input("Login.AzureAD.EmployeeID", "Employee ID"),
                                                        input("Login.AzureAD.Department", "Department"),
                                                        input("Login.AzureAD.Account_name", "Account name"),
                                                        input("Login.AzureAD.Job_title", "Job title"),
                                                        group("Nearest manager", List.of(
                                                                input("Login.AzureAD.Manager.FirstName", "First name"),
                                                                input("Login.AzureAD.Manager.LastName", "Surname"),
                                                                input("Login.AzureAD.Manager.Email", "Email"),
                                                                input("Login.AzureAD.Manager.MobilePhone", "Mobile phone"),
                                                                input("Login.AzureAD.Manager.BusinessPhones", "Work phone"),
                                                                input("Login.AzureAD.Manager.UPN", "UPN (Username)")
                                                        ))
                                                ))
                                        )),
                                        group("From integrations", List.of()),
                                        group("From dataset", List.of())
                                ))
                                .build()
                )
                .steps(List.of(
                        AcosFormStep
                                .builder()
                                .displayName("Skjemasteg")
                                .elements(List.of(
                                        input("Skjemasteg.Felt_utenfor_gr", "Felt utenfor gruppe"),
                                        group("Gruppe", List.of(
                                                input("Skjemasteg.Gruppe.Felt_i_gruppe", "Felt i gruppe")
                                        )),
                                        group("Gruppe med gruppe", List.of(
                                                group("Undefined label 1", List.of(
                                                        dropdown("Skjemasteg.Gruppe_med_grup.Organisasjon.Organisasjonsna", "Organisasjonsnavn"),
                                                        input("Skjemasteg.Gruppe_med_grup.Organisasjon.Organisasjonsnu", "Organisasjonsnummer")
                                                ))
                                        ))
                                ))
                                .build()
                ))
                .build();

        IntegrationMetadata result = acosFormDefinitionMapper.toIntegrationMetadata(1L, definition);

        assertThat(result.getSourceApplicationIntegrationId()).isEqualTo("ACOS-11");
        assertThat(result.getIntegrationDisplayName()).isEqualTo("FINT-test");
        assertThat(result.getVersion()).isEqualTo(5L);

        InstanceMetadataContent content = result.getInstanceMetadata();
        InstanceMetadataCategory savedValuesCategory = findCategory(content.getCategories(), "SavedValues");
        InstanceMetadataCategory loggedInUserCategory = findCategory(savedValuesCategory.getContent().getCategories(), "From logged in user");
        InstanceMetadataCategory idPortenCategory = findCategory(loggedInUserCategory.getContent().getCategories(), "ID Porten");
        InstanceMetadataCategory folkeregisterCategory = findCategory(idPortenCategory.getContent().getCategories(), "Folkeregister");
        assertThat(findValue(folkeregisterCategory.getContent().getInstanceValueMetadata(), "skjema.Login.IDPorten.Folkeregister.Fødselsnummer"))
                .isPresent();

        InstanceMetadataCategory entraIdCategory = findCategory(loggedInUserCategory.getContent().getCategories(), "Entra ID");
        InstanceMetadataCategory managerCategory = findCategory(entraIdCategory.getContent().getCategories(), "Nearest manager");
        assertThat(findValue(managerCategory.getContent().getInstanceValueMetadata(), "skjema.Login.AzureAD.Manager.UPN"))
                .isPresent();

        InstanceMetadataCategory stepCategory = findCategory(content.getCategories(), "Skjemasteg");
        assertThat(findValue(stepCategory.getContent().getInstanceValueMetadata(), "skjema.Skjemasteg.Felt_utenfor_gr"))
                .isPresent();
        InstanceMetadataCategory groupWithGroup = findCategory(stepCategory.getContent().getCategories(), "Gruppe med gruppe");
        InstanceMetadataCategory undefinedLabel = findCategory(groupWithGroup.getContent().getCategories(), "Undefined label 1");
        assertThat(findValue(undefinedLabel.getContent().getInstanceValueMetadata(), "skjema.Skjemasteg.Gruppe_med_grup.Organisasjon.Organisasjonsna"))
                .isPresent();
    }

    private AcosFormElement input(String id, String displayName) {
        return AcosFormElement
                .builder()
                .id(id)
                .displayName(displayName)
                .type("InputBox")
                .build();
    }

    private AcosFormElement dropdown(String id, String displayName) {
        return AcosFormElement
                .builder()
                .id(id)
                .displayName(displayName)
                .type("DropdownList")
                .build();
    }

    private AcosFormElement group(String displayName, List<AcosFormElement> elements) {
        return AcosFormElement
                .builder()
                .displayName(displayName)
                .type("Group")
                .elements(elements)
                .build();
    }

    private InstanceMetadataCategory findCategory(Collection<InstanceMetadataCategory> categories, String displayName) {
        return categories
                .stream()
                .filter(category -> displayName.equals(category.getDisplayName()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing category: " + displayName));
    }

    private Optional<InstanceValueMetadata> findValue(Collection<InstanceValueMetadata> values, String key) {
        return values
                .stream()
                .filter(value -> key.equals(value.getKey()))
                .findFirst();
    }

}
