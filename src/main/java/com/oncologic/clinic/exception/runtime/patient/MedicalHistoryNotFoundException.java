package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class MedicalHistoryNotFoundException extends DomainNotFoundException {
    public MedicalHistoryNotFoundException(Long id) {
        super("Medical record not found with ID: " + id);
    }
}