package com.oncologic.clinic.exception.runtime;

public abstract class DomainNotFoundException extends RuntimeException {
    public DomainNotFoundException(String message) {
        super(message);
    }
}
