package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@EqualsAndHashCode
public class IntegrationMetadata {
    private final Long sourceApplicationId;
    private final String sourceApplicationIntegrationId;
    private final String sourceApplicationIntegrationUri;
    private final String integrationDisplayName;
    private final Long version;
    private final InstanceMetadataContent instanceMetadata;
}
