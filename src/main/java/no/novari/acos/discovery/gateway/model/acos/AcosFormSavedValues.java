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
public class AcosFormSavedValues {

    @NotBlank
    private String displayName;

    @Valid
    private List<AcosFormElement> elements;

}
