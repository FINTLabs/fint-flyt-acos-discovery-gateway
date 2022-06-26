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
public class AcosFormGroup {
    private String id;
    private String displayName;
    private List<AcosFormElement> elements;
}
