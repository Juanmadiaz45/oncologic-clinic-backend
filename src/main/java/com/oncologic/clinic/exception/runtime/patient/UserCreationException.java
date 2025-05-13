package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainCreationException;

public class UserCreationException extends DomainCreationException {
    public UserCreationException(String message) {
        super(message);
    }
}
