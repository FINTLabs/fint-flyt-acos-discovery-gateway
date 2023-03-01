package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode
public class InstanceMetadataCategory {
    private final String displayName;
    private final InstanceMetadataContent content;
}
