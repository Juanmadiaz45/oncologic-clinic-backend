package com.oncologic.clinic.exception.runtime.examination;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class LaboratoryNotFoundException extends DomainNotFoundException {
    public LaboratoryNotFoundException(Long id) {
        super("Laboratory not found with ID: " + id);
    }
}