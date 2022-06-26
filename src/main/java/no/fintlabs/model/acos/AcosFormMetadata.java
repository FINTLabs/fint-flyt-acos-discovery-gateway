package no.fintlabs.model.acos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcosFormMetadata {
    private String id;
    private String displayName; // TODO: 26/06/2022 DisplayName?
}
