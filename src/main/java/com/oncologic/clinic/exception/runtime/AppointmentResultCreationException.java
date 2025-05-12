package com.oncologic.clinic.exception.runtime;

public class AppointmentResultCreationException extends RuntimeException {
    public AppointmentResultCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}