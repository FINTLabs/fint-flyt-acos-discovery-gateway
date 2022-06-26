package no.fintlabs.model.acos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcosFormElement {
    private String id;
    private String displayName;
    private String type;
}
