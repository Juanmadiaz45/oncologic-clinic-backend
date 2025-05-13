package com.oncologic.clinic.exception.runtime;

public class DomainCreationException extends RuntimeException {
    public DomainCreationException(String message) {
        super(message);
    }

    public DomainCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
