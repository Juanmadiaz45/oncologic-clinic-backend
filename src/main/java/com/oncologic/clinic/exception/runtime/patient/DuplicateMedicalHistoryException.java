package com.oncologic.clinic.exception.runtime.patient;

public class DuplicateMedicalHistoryException extends RuntimeException {
    public DuplicateMedicalHistoryException(Long patientId) {
        super("The patient with ID " + patientId + " already has a registered medical history");
    }
}