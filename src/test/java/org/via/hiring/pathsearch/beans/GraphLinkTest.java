package org.via.hiring.pathsearch.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GraphLinkTest {

    private static String LINK_JSON =
            "{\n" +
            "            \"attributes\": {\n" +
            "                \"weight\": 4.032963748797786\n" +
            "            },\n" +
            "            \"source\": \"f1af81de-e981-452d-8209-2926c47c9e76\",\n" +
            "            \"target\": \"dc2c94cb-f06f-496c-8c76-3f947bc31adb\"\n" +
            "        }";
    private static ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testGraphLinkMarshallUnmarshall() throws Exception  {
        GraphLink graphLink = new GraphLink();
        graphLink.marshall(mapper.readTree(LINK_JSON));
        assertGraphLink(graphLink);

        JsonNode node = graphLink.unmarshall();
        graphLink.marshall(node);
        assertGraphLink(graphLink);

    }

    private void assertGraphLink(GraphLink graphLink) {
        Assertions.assertTrue(graphLink.isValid());
        Assertions.assertEquals("f1af81de-e981-452d-8209-2926c47c9e76", graphLink.source);
        Assertions.assertEquals("dc2c94cb-f06f-496c-8c76-3f947bc31adb", graphLink.target);
        Assertions.assertEquals(4.032963748797786, graphLink.weight);

    }
}
