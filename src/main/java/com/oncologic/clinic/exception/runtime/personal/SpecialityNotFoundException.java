package com.oncologic.clinic.exception.runtime.personal;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class SpecialityNotFoundException extends DomainNotFoundException {
    public SpecialityNotFoundException(Long id) {
        super("Speciality not found with ID: " + id);
    }

    public SpecialityNotFoundException(String message) {
        super(message);
    }
}