package com.oncologic.clinic.exception.runtime.examination;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class MedicalExaminationNotFoundException extends DomainNotFoundException {
    public MedicalExaminationNotFoundException(String id) {
        super("Medical Examination not found with ID: " + id);
    }
}