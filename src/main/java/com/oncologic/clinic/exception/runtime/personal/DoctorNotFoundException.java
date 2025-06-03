package com.oncologic.clinic.exception.runtime.personal;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class DoctorNotFoundException extends DomainNotFoundException {
    public DoctorNotFoundException(Long id) {
        super("Doctor not found with ID: " + id);
    }

    public DoctorNotFoundException(String message) {
        super(message);
    }
}