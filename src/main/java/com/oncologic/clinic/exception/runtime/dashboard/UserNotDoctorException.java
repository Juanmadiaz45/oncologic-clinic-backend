package com.oncologic.clinic.exception.runtime.dashboard;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class UserNotDoctorException extends DomainNotFoundException {
    public UserNotDoctorException(String username) {
        super("User '" + username + "' is not a doctor or does not have the required permissions to access the dashboard");
    }
}