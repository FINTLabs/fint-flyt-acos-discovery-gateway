package no.novari.acos.discovery.gateway.model.acos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcosFormStep {

    @NotBlank
    private String displayName;

    @Valid
    private List<@NotNull AcosFormGroup> groups;

}
