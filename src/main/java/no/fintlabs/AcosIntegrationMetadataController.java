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

@RestController()
@RequestMapping("/api/discovery")
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

    @PostMapping("integration-metadata")
    public ResponseEntity<IntegrationMetadata> postFormDefinition(@RequestBody AcosFormDefinition acosFormDefinition) {
        acosFormDefinitionValidator.validate(acosFormDefinition).ifPresent(
                (AcosFormDefinitionValidator.Error error) -> {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Missing fields:" + error);
                }
        );
        IntegrationMetadata integrationMetadata = acosFormDefinitionMapper.toIntegrationMetadata(acosFormDefinition);
        integrationMetadataProducerService.publishNewIntegrationMetadata(integrationMetadata);
        return ResponseEntity.ok(integrationMetadata);
    }

}
