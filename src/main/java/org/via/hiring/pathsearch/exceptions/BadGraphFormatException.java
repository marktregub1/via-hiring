package org.via.hiring.pathsearch.exceptions;

public class BadGraphFormatException extends RuntimeException {
    public BadGraphFormatException(String reason) {
        super("Bad graph input format: " + reason);
    }
}
