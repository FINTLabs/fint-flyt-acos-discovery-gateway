package no.fintlabs.model.acos;

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
    private AcosFormMetadata metadata; // TODO: 26/06/2022 Alt her er metadata, så dette kan ligge fritt og hele renames AcosFormMetadata
    private List<AcosFormStep> steps;
}
