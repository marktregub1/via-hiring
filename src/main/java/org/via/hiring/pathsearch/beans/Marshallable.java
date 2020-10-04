package org.via.hiring.pathsearch.beans;

import com.fasterxml.jackson.databind.JsonNode;

public interface Marshallable {
    public void marshall(JsonNode jsonNode);
    public JsonNode unmarshall();
}
