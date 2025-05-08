package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.entity.examination.ExaminationResult;

import java.util.List;

public interface ExaminationResultService {
    ExaminationResultResponseDTO getExaminationResultById(Long id);
    List<ExaminationResultResponseDTO> getAllExaminationResults();
    ExaminationResultResponseDTO createExaminationResult(ExaminationResultRequestDTO requestDTO);
    ExaminationResultResponseDTO updateExaminationResult(Long id, ExaminationResultUpdateDTO updateDTO);
    void deleteExaminationResult(Long id);

}
