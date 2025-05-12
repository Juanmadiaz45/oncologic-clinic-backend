package com.oncologic.clinic.exception.runtime;

public class AppointmentResultNotFoundException extends RuntimeException {
    public AppointmentResultNotFoundException(Long id) {
        super("AppointmentResult no encontrado con ID: " + id);
    }
}