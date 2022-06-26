package no.fintlabs.model.fint;

import lombok.Builder;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
public class InstanceElementMetadata {

    String id;
    String displayName;
    boolean parent;
    String type;
    List<InstanceElementMetadata> children;

    @Builder(builderMethodName = "parentElementMetadataBuilder", builderClassName = "ParentElementMetadataBuilder")
    private static InstanceElementMetadata ofChildren(String id, String displayName, List<InstanceElementMetadata> children) {
        return new InstanceElementMetadata(id, displayName, true, null, children);
    }

    @Builder(builderMethodName = "typedElementMetadataBuilder", builderClassName = "TypedElementMetadataBuilder")
    private static InstanceElementMetadata ofType(String id, String displayName, String type) {
        return new InstanceElementMetadata(id, displayName, false, type, Collections.emptyList());
    }

}
