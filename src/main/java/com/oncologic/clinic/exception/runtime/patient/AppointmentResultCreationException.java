package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainCreationException;

public class AppointmentResultCreationException extends DomainCreationException {
    public AppointmentResultCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}