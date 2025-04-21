package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.entity.patient.TypeOfTreatment;
import com.oncologic.clinic.service.patient.TypeOfTreatmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeOfTreatmentServiceImpl implements TypeOfTreatmentService {
    @Override
    public TypeOfTreatment getTypeOfTreatmentById(Long id) {
        return null;
    }

    @Override
    public List<TypeOfTreatment> getAllTypesOfTreatment() {
        return List.of();
    }

    @Override
    public TypeOfTreatment createTypeOfTreatment(TypeOfTreatment type) {
        return null;
    }

    @Override
    public TypeOfTreatment updateTypeOfTreatment(TypeOfTreatment type) {
        return null;
    }

    @Override
    public void deleteTypeOfTreatment(Long id) {

    }
}