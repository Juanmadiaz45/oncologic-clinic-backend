package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.MedicalHistoryRequestDTO;
import com.oncologic.clinic.dto.patient.response.MedicalHistoryResponseDTO;
import com.oncologic.clinic.dto.patient.update.MedicalHistoryUpdateDTO;

import java.util.List;

public interface MedicalHistoryService {
    MedicalHistoryResponseDTO getMedicalHistoryById(Long id);
    List<MedicalHistoryResponseDTO> getAllMedicalHistories();
    MedicalHistoryResponseDTO createMedicalHistory(MedicalHistoryRequestDTO medicalHistoryDTO);
    MedicalHistoryResponseDTO updateMedicalHistory(Long id, MedicalHistoryUpdateDTO medicalHistoryDTO);
    void deleteMedicalHistory(Long id);
}
