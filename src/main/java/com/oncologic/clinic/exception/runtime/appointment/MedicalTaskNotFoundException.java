package com.oncologic.clinic.exception.runtime.appointment;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class MedicalTaskNotFoundException extends DomainNotFoundException {
    public MedicalTaskNotFoundException(Long id) {
        super("Medical task not found with ID: " + id);
    }
}
