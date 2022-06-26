package no.fintlabs;

import no.fintlabs.model.acos.AcosFormDefinition;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AcosFormDefinitionValidator {

    public static class Error {
    }

    public Optional<Error> validate(AcosFormDefinition acosFormDefinition) {
        // TODO: 26/06/2022 Implement
        return Optional.empty();
    }

}
