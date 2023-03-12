package com.example.springdemo2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// cand o inregistrare nu e gasita in baza de date

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID=1L;
    public ResourceNotFoundException(String message){
        super(message);
    }
}
