package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.TypeOfTreatment;

import java.util.List;

public interface TypeOfTreatmentService {
    TypeOfTreatment getTypeOfTreatmentById(Long id);
    List<TypeOfTreatment> getAllTypesOfTreatment();
    TypeOfTreatment createTypeOfTreatment(TypeOfTreatment type);
    TypeOfTreatment updateTypeOfTreatment(TypeOfTreatment type);
    void deleteTypeOfTreatment(Long id);
}
