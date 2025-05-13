package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class PatientNotFoundException extends DomainNotFoundException {
    public PatientNotFoundException(Long id) {
        super("Patient not found with ID: " + id);
    }
}