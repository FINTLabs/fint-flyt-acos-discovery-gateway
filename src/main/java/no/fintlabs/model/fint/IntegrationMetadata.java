package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IntegrationMetadata {
    private final String sourceApplicationId;
    private final String sourceApplicationIntegrationId;
    private final String sourceApplicationIntegrationUri;
    private final String integrationDisplayName;
    private final List<InstanceElementMetadata> instanceElementMetadata;
}
