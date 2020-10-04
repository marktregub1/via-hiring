package org.via.hiring.pathsearch.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GraphDataTest {
    String GRAPH_DATA =
            "{\n" +
            "    \"directed\": true,\n" +
            "    \"graph\": {\n" +
            "        \"name\": \"test-graph\",\n" +
            "        \"version\": 1\n" +
            "    },\n" +
            "\n" +
            "    \"links\": [\n" +
            "        {\n" +
            "            \"attributes\": {\n" +
            "                \"weight\": 4.032963748797786\n" +
            "            },\n" +
            "            \"source\": \"bd0cc0ba-6db4-4f16-916b-c09e133c2be8\",\n" +
            "            \"target\": \"a8257129-7372-4bd1-a612-faa7dd4f857e\"\n" +
            "        }\n" +
            "    ],\n" +
            "\n" +
            "    \"nodes\": [\n" +
            "        {\n" +
            "            \"id\": \"bd0cc0ba-6db4-4f16-916b-c09e133c2be8\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": \"a8257129-7372-4bd1-a612-faa7dd4f857e\"\n" +
            "        }\n" +
            "   ]\n" +
            "}";

    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGraphDataMarshallUnmarshall() throws Exception {
        GraphData graphData = new GraphData();
        graphData.marshall(mapper.readTree(GRAPH_DATA));
        assertGraphData(graphData);

        JsonNode node = graphData.unmarshall();
        graphData = new GraphData();
        graphData.marshall(node);
        assertGraphData(graphData);
    }

    private void assertGraphData(GraphData graphData) {
        Assertions.assertEquals(true, graphData.directed);
        Assertions.assertEquals("test-graph", graphData.name);
        Assertions.assertEquals("1", graphData.version);
        Assertions.assertEquals(1, graphData.links.size());
        Assertions.assertEquals(2, graphData.nodes.size());
    }
}
