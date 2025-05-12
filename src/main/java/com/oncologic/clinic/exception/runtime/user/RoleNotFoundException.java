package com.oncologic.clinic.exception.runtime.user;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class RoleNotFoundException extends DomainNotFoundException {
    public RoleNotFoundException(Long id) {
        super("Role not found with ID: " + id);
    }

    public RoleNotFoundException(String message) {
        super(message);
    }
}