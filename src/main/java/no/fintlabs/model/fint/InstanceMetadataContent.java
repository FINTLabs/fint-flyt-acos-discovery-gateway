package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collection;

@Builder
@EqualsAndHashCode
public class InstanceMetadataContent {

    @Builder.Default
    private final Collection<InstanceValueMetadata> instanceValueMetadata = new ArrayList<>();

    @Builder.Default
    private final Collection<InstanceObjectCollectionMetadata> instanceObjectCollectionMetadata = new ArrayList<>();

    @Builder.Default
    private final Collection<InstanceMetadataCategory> categories = new ArrayList<>();

}
