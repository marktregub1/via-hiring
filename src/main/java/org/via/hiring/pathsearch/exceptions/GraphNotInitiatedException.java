package org.via.hiring.pathsearch.exceptions;

public class GraphNotInitiatedException extends RuntimeException {
    public GraphNotInitiatedException() {
        super("You should call /set-graph API first");
    }
}
