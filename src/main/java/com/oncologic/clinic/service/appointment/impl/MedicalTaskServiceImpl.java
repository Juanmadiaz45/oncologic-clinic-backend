package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.service.appointment.MedicalTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalTaskServiceImpl implements MedicalTaskService {
    @Override
    public MedicalTask getMedicalTaskById(Long id) {
        return null;
    }

    @Override
    public List<MedicalTask> getAllMedicalTasks() {
        return List.of();
    }

    @Override
    public MedicalTask createMedicalTask(MedicalTask task) {
        return null;
    }

    @Override
    public MedicalTask updateMedicalTask(MedicalTask task) {
        return null;
    }

    @Override
    public void deleteMedicalTask(Long id) {

    }
}