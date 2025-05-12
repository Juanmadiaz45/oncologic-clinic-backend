package com.oncologic.clinic.exception.runtime;

public class MedicalHistoryNotFoundException extends RuntimeException {
    public MedicalHistoryNotFoundException(Long id) {
        super("Historial médico no encontrado con ID: " + id);
    }
}