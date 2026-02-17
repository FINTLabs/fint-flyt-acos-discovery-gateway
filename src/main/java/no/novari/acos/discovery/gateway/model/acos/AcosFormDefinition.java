package no.novari.acos.discovery.gateway.model.acos;

import jakarta.validation.Valid;
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
public class AcosFormDefinition {

    @NotNull
    @Valid
    private AcosFormMetadata metadata;

    @Valid
    private AcosFormSavedValues savedValues;

    @Valid
    private List<@NotNull AcosFormStep> steps;

}
