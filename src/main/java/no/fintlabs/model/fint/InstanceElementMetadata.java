package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
public class InstanceElementMetadata {

    String id;
    String displayName;
    List<InstanceElementMetadata> children;

    @Builder
    public static InstanceElementMetadata builderMethod(String id, String displayName, List<InstanceElementMetadata> children) {
        return new InstanceElementMetadata(id, displayName, children != null ? children : Collections.emptyList());
    }
}
