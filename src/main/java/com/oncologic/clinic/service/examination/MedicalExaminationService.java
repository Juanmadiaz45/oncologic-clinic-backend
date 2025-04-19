package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.entity.examination.MedicalExamination;

import java.util.List;

public interface MedicalExaminationService {
    MedicalExamination getMedicalExaminationById(Long id);
    List<MedicalExamination> getAllMedicalExaminations();
    MedicalExamination createMedicalExamination(MedicalExamination exam);
    MedicalExamination updateMedicalExamination(MedicalExamination exam);
    void deleteMedicalExamination(Long id);
}
