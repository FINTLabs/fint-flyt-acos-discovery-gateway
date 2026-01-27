package no.novari.acos.discovery.gateway.model.acos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcosFormElement {

    private String id;

    @NotBlank
    private String displayName;

    @NotBlank
    private String type;

    @Valid
    private List<AcosFormElement> elements;

}
