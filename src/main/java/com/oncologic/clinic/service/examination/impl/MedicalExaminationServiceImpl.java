package com.oncologic.clinic.service.examination.impl;


import com.oncologic.clinic.entity.examination.MedicalExamination;
import com.oncologic.clinic.service.examination.MedicalExaminationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalExaminationServiceImpl implements MedicalExaminationService {
    @Override
    public MedicalExamination getMedicalExaminationById(Long id) {
        return null;
    }

    @Override
    public List<MedicalExamination> getAllMedicalExaminations() {
        return List.of();
    }

    @Override
    public MedicalExamination createMedicalExamination(MedicalExamination exam) {
        return null;
    }

    @Override
    public MedicalExamination updateMedicalExamination(MedicalExamination exam) {
        return null;
    }

    @Override
    public void deleteMedicalExamination(Long id) {

    }
}