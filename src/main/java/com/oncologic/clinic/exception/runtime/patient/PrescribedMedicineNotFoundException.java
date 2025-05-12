package com.oncologic.clinic.exception.runtime.patient;

public class PrescribedMedicineNotFoundException extends RuntimeException {
    public PrescribedMedicineNotFoundException(Long id) {
        super("Prescription medicine not found with ID: " + id);
    }
}