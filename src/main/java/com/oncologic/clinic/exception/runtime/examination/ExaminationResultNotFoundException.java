package com.oncologic.clinic.exception.runtime.examination;

import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class ExaminationResultNotFoundException extends DomainNotFoundException {
    public ExaminationResultNotFoundException(Long id) {
        super("Examination result not found with ID: " + id);
    }
}
