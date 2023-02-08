package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class InstanceElementMetadata {

    public enum Type {
        STRING, BOOLEAN, INTEGER, DOUBLE, FILE
    }

    private final String key;
    private final Type type;
    private final String displayName;
    private final boolean disabled;
    private final List<InstanceElementMetadata> children;

    @Builder
    public static InstanceElementMetadata builderMethod(String key, Type type, String displayName, boolean disabled, List<InstanceElementMetadata> children) {
        return new InstanceElementMetadata(key, type, displayName, disabled, children != null ? children : Collections.emptyList());
    }

}
