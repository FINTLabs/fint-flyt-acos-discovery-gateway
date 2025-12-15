package no.novari.acos.discovery.gateway.model.acos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcosFormMetadata {

    @NotBlank
    private String formId;

    @NotBlank
    private String formDisplayName;

    // TODO: 01/07/2022 Add validation when ACOS has added support for form URI
    private String formUri;

    @NotNull
    private Long version;

}
