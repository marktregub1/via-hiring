package org.via.hiring.pathsearch.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.graph.builder.GraphTypeBuilder;
import org.via.hiring.pathsearch.exceptions.BadGraphFormatException;
import org.via.hiring.pathsearch.exceptions.IllegalSearchParametersException;

import static org.via.hiring.pathsearch.utils.JsonNodeUtils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphData implements Marshallable {

    Boolean directed;
    String name;
    String version;
    Boolean multiGraph;
    List<String> nodes = new ArrayList<>();
    List<GraphLink> links = new ArrayList<>();

    private Graph<String, DefaultWeightedEdge> jGraph;


    public static GraphData fromJson(JsonNode node) {
        GraphData graphData = new GraphData();
        graphData.marshall(node);
        return graphData;
    }

    @Override
    public void marshall(JsonNode jsonNode) {
        directed = getBooleanValue(jsonNode, "directed");
        multiGraph = getBooleanValue(jsonNode, "multigraph");
        name = getStringValue(jsonNode, "graph", "name");
        version = getStringValue(jsonNode, "graph", "version");

        links = getArray(jsonNode, "links", GraphLink.class);
        nodes = getArray(jsonNode, "nodes", "id");

        jGraph = createJGraph();
        GraphBuilder builder = new GraphBuilder<>(jGraph);
        links.forEach(link -> builder.addEdge(link.source, link.target, link.weight.doubleValue()));
    }

    public List<String> searchPath(String from, String to) {
        try {
            return new DijkstraShortestPath(jGraph).getPath(from, to).getVertexList();
        } catch(Exception e) {
            throw new IllegalSearchParametersException(from, to, e);
        }
    }

    public void setWeight(String from, String to, Double weight) {
        Optional<DefaultWeightedEdge> edge = jGraph.edgeSet().stream().filter(e ->
                jGraph.getEdgeSource(e).equals(from) && jGraph.getEdgeTarget(e).equals(to))
                .findFirst();
        if(!edge.isPresent()) {
            throw new IllegalSearchParametersException(from, to);
        }
        jGraph.setEdgeWeight(edge.get(), weight);
    }

    @Override
    public JsonNode unmarshall() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();

        objectNode.put("directed", directed);

        ObjectNode graphNode = mapper.createObjectNode();
        graphNode.put("name", name);
        graphNode.put("version", version);
        objectNode.set("graph", graphNode);

        objectNode.put("multigraph", multiGraph);

        objectNode.set("links", toArrayNode(links, mapper));
        objectNode.set("nodes", toArrayNode(nodes, "id", mapper));

        return objectNode;
    }


    public void validate() {
        String errMsg = "";
        if(directed == null) errMsg = "Missing or invalid value for field \"directed\"";
        if(name == null) errMsg = "Missing or invalid value for field \"name\"";
        if(links == null || links.isEmpty()) errMsg = "Graph provided with no links or with invalid values for links";

        if(!errMsg.isEmpty()) throw new BadGraphFormatException(errMsg);
    }

    private Graph<String, DefaultWeightedEdge> createJGraph() {
        GraphTypeBuilder<String, DefaultWeightedEdge> typeBuilder=
                Boolean.TRUE.equals(directed) ? GraphTypeBuilder.directed() : GraphTypeBuilder.undirected();
        return typeBuilder
                .allowingMultipleEdges(Boolean.TRUE.equals(multiGraph))
                .allowingSelfLoops(false)
                .edgeClass(DefaultWeightedEdge.class)
                .weighted(true)
                .buildGraph();
    }

}
