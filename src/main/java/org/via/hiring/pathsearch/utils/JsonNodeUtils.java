package org.via.hiring.pathsearch.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.via.hiring.pathsearch.beans.Marshallable;

import java.util.ArrayList;
import java.util.List;

public class JsonNodeUtils {
    public static String getStringValue(JsonNode node, String... jpath) {
        JsonNode currNode = path(node, jpath);
        String fieldName = jpath[jpath.length-1];
        return currNode != null && currNode.has(fieldName) ? currNode.get(fieldName).asText() : null;
    }

    public static Double getDoubleValue(JsonNode node, String... jpath) {
        JsonNode currNode = path(node, jpath);
        String fieldName = jpath[jpath.length-1];
        return currNode != null && currNode.has(fieldName) ? currNode.get(fieldName).asDouble() : null;
    }

    public static Boolean getBooleanValue(JsonNode node, String... jpath) {
        JsonNode currNode = path(node, jpath);
        String fieldName = jpath[jpath.length-1];
        return currNode != null && currNode.has(fieldName) ? currNode.get(fieldName).asBoolean() : null;
    }

    public static <T extends Marshallable> List<T> getArray(JsonNode root, String fieldName, Class clazz) {
        JsonNode node = root.get(fieldName);
        if(node == null || !node.isArray()) return null;

        List<T> res = new ArrayList<>();
        ArrayNode arrayNode = (ArrayNode) node;
        for(int i = 0; i < arrayNode.size(); i++) {
            T item;
            try {
                item = (T) clazz.newInstance();
            } catch(Exception e) {
                return null;
            }
            item.marshall(arrayNode.get(i));
            res.add(item);
        }

        return res;
    }


    public static List<String> getArray(JsonNode root, String arrayField, String internalField) {
        JsonNode node = root.get(arrayField);
        if(node == null || !node.isArray()) return null;

        List<String> res = new ArrayList<>();
        ArrayNode arrayNode = (ArrayNode) node;
        for(int i = 0; i < arrayNode.size(); i++) {
            res.add(arrayNode.get(i).get(internalField).asText());
        }

        return res;
    }

    public static <T extends Marshallable> ArrayNode toArrayNode(List<T> list, ObjectMapper mapper) {
        ArrayNode arrayNode = mapper.createArrayNode();
        list.forEach(item -> arrayNode.add(item.unmarshall()));
        return arrayNode;
    }

    public static ArrayNode toArrayNode(List<String> list, String fieldName, ObjectMapper mapper) {
        ArrayNode arrayNode = mapper.createArrayNode();
        list.forEach(item -> {
            ObjectNode node = mapper.createObjectNode();
            node.put(fieldName, item);
            arrayNode.add(node);
        });
        return arrayNode;
    }

    private static JsonNode path(JsonNode node, String... fieldNames) {
        if(node == null) return null;

        JsonNode currNode = node;
        for(int i=0; i<fieldNames.length-1; i++) {
            currNode = node.get(fieldNames[i]);
            if(currNode == null) break;
        }
        return currNode;
    }
}
