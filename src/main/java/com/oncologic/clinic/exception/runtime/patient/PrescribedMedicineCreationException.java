package com.oncologic.clinic.exception.runtime.patient;

import com.oncologic.clinic.exception.runtime.DomainCreationException;

public class PrescribedMedicineCreationException extends DomainCreationException {
    public PrescribedMedicineCreationException(String message) {
        super(message);
    }
}