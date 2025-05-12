package com.oncologic.clinic.exception.runtime.examination;


import com.oncologic.clinic.exception.runtime.DomainNotFoundException;

public class TypeOfExamNotFoundException extends DomainNotFoundException {
    public TypeOfExamNotFoundException(Long id) {
        super("Type Of Exam not found with ID: " + id);
    }
}