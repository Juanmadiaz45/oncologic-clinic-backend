package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class ResourceNotFoundException extends DomainNotFoundException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
