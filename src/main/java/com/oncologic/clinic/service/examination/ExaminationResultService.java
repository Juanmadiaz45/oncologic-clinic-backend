package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.entity.examination.ExaminationResult;

import java.util.List;

public interface ExaminationResultService {
    ExaminationResult getExaminationResultById(Long id);
    List<ExaminationResult> getAllExaminationResults();
    ExaminationResult createExaminationResult(ExaminationResult result);
    ExaminationResult updateExaminationResult(ExaminationResult result);
    void deleteExaminationResult(Long id);

}
