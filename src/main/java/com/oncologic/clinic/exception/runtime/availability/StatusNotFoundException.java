package com.oncologic.clinic.exception.runtime.availability;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class StatusNotFoundException extends DomainNotFoundException {
    public StatusNotFoundException(String message) {
        super(message);
    }
}
