package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;

import java.util.List;
import java.util.Map;

public interface MedicalExaminationManagementService {

    MedicalExaminationResponseDTO createExaminationForPatient(Long patientId, MedicalExaminationRequestDTO requestDTO);


    List<MedicalExaminationResponseDTO> getExaminationsByPatientId(Long patientId);


    Map<String, Object> getExaminationDetailsWithResults(String examinationId);


    MedicalExaminationResponseDTO updateExamination(String examinationId, MedicalExaminationUpdateDTO updateDTO);


    void deleteExamination(String examinationId);


    Map<String, Object> getCompleteMedicalHistoryWithExaminations(Long patientId);
}
