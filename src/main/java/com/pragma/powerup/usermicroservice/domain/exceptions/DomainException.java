package com.pragma.powerup.usermicroservice.domain.exceptions;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
