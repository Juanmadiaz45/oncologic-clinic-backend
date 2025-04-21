package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.service.examination.TypeOfExamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeOfExamServiceImpl implements TypeOfExamService {
    @Override
    public TypeOfExam getTypeOfExamById(Long id) {
        return null;
    }

    @Override
    public List<TypeOfExam> getAllTypesOfExam() {
        return List.of();
    }

    @Override
    public TypeOfExam createTypeOfExam(TypeOfExam type) {
        return null;
    }

    @Override
    public TypeOfExam updateTypeOfExam(TypeOfExam type) {
        return null;
    }

    @Override
    public void deleteTypeOfExam(Long id) {

    }
}