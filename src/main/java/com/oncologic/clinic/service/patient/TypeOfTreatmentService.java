package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.TypeOfTreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TypeOfTreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TypeOfTreatmentUpdateDTO;

import java.util.List;

public interface TypeOfTreatmentService {
    TypeOfTreatmentResponseDTO getTypeOfTreatmentById(Long id);
    List<TypeOfTreatmentResponseDTO> getAllTypesOfTreatment();
    TypeOfTreatmentResponseDTO createTypeOfTreatment(TypeOfTreatmentRequestDTO typeOfTreatmentDTO);
    TypeOfTreatmentResponseDTO updateTypeOfTreatment(Long id, TypeOfTreatmentUpdateDTO typeOfTreatmentDTO);
    void deleteTypeOfTreatment(Long id);
}
