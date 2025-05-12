package com.oncologic.clinic.exception.runtime.personal;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class AdministrativeNotFoundException extends DomainNotFoundException {
    public AdministrativeNotFoundException(Long id) {
        super("Administrative not found with ID: " + id);
    }
}