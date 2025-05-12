package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class TreatmentNotFoundException extends DomainNotFoundException {
    public TreatmentNotFoundException(Long id) {
        super("Treatment not found with ID: " + id);
    }
}
