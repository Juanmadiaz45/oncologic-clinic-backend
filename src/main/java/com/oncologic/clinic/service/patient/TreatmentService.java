package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.TreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TreatmentUpdateDTO;
import com.oncologic.clinic.entity.patient.Treatment;

import java.util.List;

public interface TreatmentService {
    TreatmentResponseDTO getTreatmentById(Long id);
    List<TreatmentResponseDTO> getAllTreatments();
    TreatmentResponseDTO createTreatment(TreatmentRequestDTO treatmentDTO);
    TreatmentResponseDTO updateTreatment(Long id, TreatmentUpdateDTO treatmentDTO);
    void deleteTreatment(Long id);
}
