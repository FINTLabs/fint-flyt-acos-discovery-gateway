package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class IntegrationMetadata {
    String id;
    String displayName;
    List<InstanceElementMetadata> instanceElementMetadata;
}
