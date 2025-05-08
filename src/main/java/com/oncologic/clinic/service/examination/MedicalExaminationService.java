package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.entity.examination.MedicalExamination;

import java.util.List;

public interface MedicalExaminationService {
    MedicalExaminationResponseDTO getMedicalExaminationById(String id);
    List<MedicalExaminationResponseDTO> getAllMedicalExaminations();
    MedicalExaminationResponseDTO createMedicalExamination(MedicalExaminationRequestDTO requestDTO);
    MedicalExaminationResponseDTO updateMedicalExamination(String id, MedicalExaminationUpdateDTO updateDTO) ;
    void deleteMedicalExamination(String id);
}
