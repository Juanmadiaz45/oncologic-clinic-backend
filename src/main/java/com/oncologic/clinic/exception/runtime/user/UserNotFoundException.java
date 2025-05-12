package com.oncologic.clinic.exception.runtime.user;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class UserNotFoundException extends DomainNotFoundException {
    public UserNotFoundException(Long id) {
        super("User not found with ID: " + id);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}