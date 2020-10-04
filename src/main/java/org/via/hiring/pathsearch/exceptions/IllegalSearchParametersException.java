package org.via.hiring.pathsearch.exceptions;

public class IllegalSearchParametersException extends RuntimeException {
    public IllegalSearchParametersException(String from, String to, Exception e) {
        super("Edge not found: " + from + " --> " + to, e );
    }

    public IllegalSearchParametersException(String from, String to) {
        super("Edge not found: " + from + " --> " + to);
    }
}
