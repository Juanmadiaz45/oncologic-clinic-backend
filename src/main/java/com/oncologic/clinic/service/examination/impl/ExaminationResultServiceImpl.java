package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.entity.examination.ExaminationResult;
import com.oncologic.clinic.service.examination.ExaminationResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExaminationResultServiceImpl implements ExaminationResultService {

    @Override
    public ExaminationResult getExaminationResultById(Long id) {
        return null;
    }

    @Override
    public List<ExaminationResult> getAllExaminationResults() {
        return List.of();
    }

    @Override
    public ExaminationResult createExaminationResult(ExaminationResult result) {
        return null;
    }

    @Override
    public ExaminationResult updateExaminationResult(ExaminationResult result) {
        return null;
    }

    @Override
    public void deleteExaminationResult(Long id) {

    }
}