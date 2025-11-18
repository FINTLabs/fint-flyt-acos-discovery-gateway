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
public class AcosFormElement {

    @NotBlank
    private String id;

    @NotNull
    private String displayName;

    @NotBlank
    private String type;

}
