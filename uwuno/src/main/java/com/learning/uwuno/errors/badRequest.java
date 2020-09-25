package com.learning.uwuno.errors;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class badRequest extends RuntimeException {
    public badRequest(String errorMessage){
        super(errorMessage);
    }
}
