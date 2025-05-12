package com.oncologic.clinic.exception.runtime.appointment;


import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class MedicalAppointmentNotFoundException extends DomainNotFoundException {
    public MedicalAppointmentNotFoundException(Long id) {
        super("Medical appointment not found with ID: " + id);
    }
}
