package com.websystique.springmvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NonUniqueSsoIdException extends RuntimeException {

    public NonUniqueSsoIdException(String message ) {
        super(message);
    }
}
