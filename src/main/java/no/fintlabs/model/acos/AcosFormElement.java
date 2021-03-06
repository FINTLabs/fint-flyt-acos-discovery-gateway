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
public class AcosFormElement {

    @NotBlank
    private String id;

    @NotBlank
    private String displayName;

    @NotBlank
    private String type;

}
