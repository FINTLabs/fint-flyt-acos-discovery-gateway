package no.fintlabs;

import no.fintlabs.model.acos.AcosFormDefinition;
import no.fintlabs.model.fint.IntegrationMetadata;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping("/api/integrasjon/metadata/acos")
public class AcosIntegrationMetadataController {

    private final AcosFormDefinitionMapper acosFormDefinitionMapper;
    private final AcosFormDefinitionValidator acosFormDefinitionValidator;
    private final IntegrationMetadataProducerService integrationMetadataProducerService;

    public AcosIntegrationMetadataController(
            AcosFormDefinitionMapper acosFormDefinitionMapper,
            AcosFormDefinitionValidator acosFormDefinitionValidator,
            IntegrationMetadataProducerService integrationMetadataProducerService
    ) {
        this.acosFormDefinitionMapper = acosFormDefinitionMapper;
        this.acosFormDefinitionValidator = acosFormDefinitionValidator;
        this.integrationMetadataProducerService = integrationMetadataProducerService;
    }

    @PostMapping()
    public ResponseEntity<?> postIntegrationMetadata(@RequestBody AcosFormDefinition acosFormDefinition) {
        acosFormDefinitionValidator.validate(acosFormDefinition).ifPresent(
                (List<String> validationErrors) -> {
                    throw new ResponseStatusException(
                            HttpStatus.UNPROCESSABLE_ENTITY, "Validation error(s): "
                            + validationErrors.stream().map(error -> "'" + error + "'").toList()
                    );
                }
        );
        IntegrationMetadata integrationMetadata = acosFormDefinitionMapper.toIntegrationMetadata(acosFormDefinition);
        integrationMetadataProducerService.publishNewIntegrationMetadata(integrationMetadata);
        return ResponseEntity.accepted().build();
    }

}
