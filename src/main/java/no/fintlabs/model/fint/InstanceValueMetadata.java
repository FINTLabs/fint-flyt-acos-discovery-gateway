package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class InstanceValueMetadata {

    public enum Type {
        STRING,
        BOOLEAN,
        FILE
    }

    private final String displayName;
    private final Type type;
    private final String key;

}
