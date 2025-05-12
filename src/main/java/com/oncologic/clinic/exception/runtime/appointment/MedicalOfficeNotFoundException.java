package com.oncologic.clinic.exception.runtime.appointment;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class MedicalOfficeNotFoundException extends DomainNotFoundException {
    public MedicalOfficeNotFoundException(Long id) {
        super("Medical office not found with ID: " + id);
    }
}