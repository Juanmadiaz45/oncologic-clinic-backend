package com.oncologic.clinic.exception.runtime;

public class AppointmentResultUpdateException extends RuntimeException {
    public AppointmentResultUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}