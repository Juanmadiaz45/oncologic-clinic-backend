package com.oncologic.clinic.exception.runtime.personal;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class PersonalNotFoundException extends DomainNotFoundException {
    public PersonalNotFoundException(String message) {
        super(message);
    }

}