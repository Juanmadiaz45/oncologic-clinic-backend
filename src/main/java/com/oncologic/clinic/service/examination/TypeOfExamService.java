package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.entity.examination.TypeOfExam;

import java.util.List;

public interface TypeOfExamService {
    TypeOfExam getTypeOfExamById(Long id);
    List<TypeOfExam> getAllTypesOfExam();
    TypeOfExam createTypeOfExam(TypeOfExam type);
    TypeOfExam updateTypeOfExam(TypeOfExam type);
    void deleteTypeOfExam(Long id);
}
