package no.fintlabs.model.acos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
    private List<@NotNull AcosFormStep> steps;

}
