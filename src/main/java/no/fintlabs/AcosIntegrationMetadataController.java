package no.fintlabs;

import lombok.extern.slf4j.Slf4j;
import no.fintlabs.model.acos.AcosFormDefinition;
import no.fintlabs.model.fint.IntegrationMetadata;
import no.fintlabs.resourceserver.security.client.ClientAuthorizationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;

import static no.fintlabs.resourceserver.UrlPaths.EXTERNAL_API;

@Slf4j
@RestController
@RequestMapping(EXTERNAL_API + "/acos/metadata")
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
    public Mono<ResponseEntity<?>> postIntegrationMetadata(
            @RequestBody AcosFormDefinition acosFormDefinition,
            @AuthenticationPrincipal Mono<Authentication> authenticationMono
    ) {
        return authenticationMono.map(authentication -> processIntegrationMetadata(acosFormDefinition, authentication));
    }

    public ResponseEntity<?> processIntegrationMetadata(AcosFormDefinition acosFormDefinition, Authentication authentication) {
        log.info("Received acos form definition: {}", acosFormDefinition);
        acosFormDefinitionValidator.validate(acosFormDefinition).ifPresent(
                (List<String> validationErrors) -> {
                    throw new ResponseStatusException(
                            HttpStatus.UNPROCESSABLE_ENTITY, "Validation error(s): "
                            + validationErrors.stream().map(error -> "'" + error + "'").toList()
                    );
                }
        );
        IntegrationMetadata integrationMetadata = acosFormDefinitionMapper.toIntegrationMetadata(
                ClientAuthorizationUtil.getSourceApplicationId(authentication),
                acosFormDefinition
        );
        integrationMetadataProducerService.publishNewIntegrationMetadata(integrationMetadata);
        return ResponseEntity.accepted().build();
    }

}
