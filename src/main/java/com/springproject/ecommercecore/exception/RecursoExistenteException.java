package com.springproject.ecommercecore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class RecursoExistenteException extends RuntimeException {
    public RecursoExistenteException(String message) {
        super(message);
    }
}
