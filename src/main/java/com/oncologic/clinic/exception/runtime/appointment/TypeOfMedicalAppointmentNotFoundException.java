package com.oncologic.clinic.exception.runtime.appointment;


import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class TypeOfMedicalAppointmentNotFoundException extends DomainNotFoundException {
    public TypeOfMedicalAppointmentNotFoundException(Long id) {
        super("Type of appointment not found with ID: " + id);
    }
}
