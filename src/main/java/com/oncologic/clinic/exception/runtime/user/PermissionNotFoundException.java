package com.oncologic.clinic.exception.runtime.user;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class PermissionNotFoundException extends DomainNotFoundException {
    public PermissionNotFoundException(Long id) {
        super("Permission not found with ID: " + id);
    }
}