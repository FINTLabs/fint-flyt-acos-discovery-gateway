package no.fintlabs.model.acos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcosFormMetadata {

    @NotBlank
    private String formId;

    @NotBlank
    private String formDisplayName;

    @NotBlank
    private String formUri;

}
