package org.via.hiring.pathsearch.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GraphNotInitializedAdvice {
    @ResponseBody
    @ExceptionHandler(GraphNotInitiatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String graphNotInitiatedExceptionHandler(GraphNotInitiatedException ex) {
        return ex.getMessage();
    }
}
