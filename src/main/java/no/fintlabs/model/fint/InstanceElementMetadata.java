package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class InstanceElementMetadata {

    private final String key;
    private final String displayName;
    private final List<InstanceElementMetadata> children;

    @Builder
    public static InstanceElementMetadata builderMethod(String key, String displayName, List<InstanceElementMetadata> children) {
        return new InstanceElementMetadata(key, displayName, children != null ? children : Collections.emptyList());
    }
}
