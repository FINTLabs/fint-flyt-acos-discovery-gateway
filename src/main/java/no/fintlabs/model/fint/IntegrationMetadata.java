package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class IntegrationMetadata {
    String sourceApplicationIntegrationId;
    String sourceApplicationIntegrationUri;
    String integrationDisplayName;
    List<InstanceElementMetadata> instanceElementMetadata;
}
