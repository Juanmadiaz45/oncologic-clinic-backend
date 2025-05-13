package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class PrescribedMedicineNotFoundException extends DomainNotFoundException {
    public PrescribedMedicineNotFoundException(Long id) {
        super("Prescription medicine not found with ID: " + id);
    }
}