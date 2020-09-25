package com.learning.uwuno.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class errorMessage {
    // Class Variables
    private String timestamp;
    private String status;
    private String message;
    private String path;

    public errorMessage(String errorMessage, String status, String path) {
        timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        this.status = status;
        message = errorMessage;
        this.path = path;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }
}

@ControllerAdvice
public class exceptionHandler extends ResponseEntityExceptionHandler {
    // Error 404
    @ExceptionHandler(errorNotFound.class)
    public ResponseEntity<errorMessage> handleErrorNotFoundException(errorNotFound e, WebRequest request) {
        errorMessage errorMessage = new errorMessage(e.getLocalizedMessage(), "404",
                                    ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return new ResponseEntity<errorMessage>(errorMessage, HttpStatus.NOT_FOUND);
    }

    // Error 400
    @ExceptionHandler(badRequest.class)
    public ResponseEntity<errorMessage> handleBadRequestException(badRequest e, WebRequest request) {
        errorMessage errorMessage = new errorMessage(e.getLocalizedMessage(), "400",
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return new ResponseEntity<errorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    // Error 500
    @ExceptionHandler(internalServerError.class)
    public ResponseEntity<errorMessage> handleInternalServerErrorException(badRequest e, WebRequest request) {
        errorMessage errorMessage = new errorMessage(e.getLocalizedMessage(), "500",
                ((ServletWebRequest)request).getRequest().getRequestURL().toString());
        return new ResponseEntity<errorMessage>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
