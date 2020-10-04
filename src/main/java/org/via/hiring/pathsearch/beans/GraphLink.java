package org.via.hiring.pathsearch.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static org.via.hiring.pathsearch.utils.JsonNodeUtils.*;


public class GraphLink implements Marshallable {
    String source;
    String target;
    Double weight;

    @Override
    public void marshall(JsonNode jsonNode) {
        this.source = getStringValue(jsonNode, "source");
        this.target = getStringValue(jsonNode, "target");
        this.weight = getDoubleValue(jsonNode, "attributes", "weight");

        if(!isValid())
            throw new IllegalArgumentException("Illegal link json: " + jsonNode.toPrettyString());
    }

    @Override
    public JsonNode unmarshall() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("source", source);
        objectNode.put("target", target);
        ObjectNode attributes = mapper.createObjectNode();
        attributes.put("weight", weight);
        objectNode.set("attributes", attributes);

        return objectNode;
    }

    boolean isValid() {
        return !StringUtils.isEmpty(source) && !StringUtils.isEmpty(target) && weight != null && weight > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GraphLink)) return false;
        GraphLink graphLink = (GraphLink) o;
        return Objects.equals(source, graphLink.source) &&
                Objects.equals(target, graphLink.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, target, weight);
    }
}
