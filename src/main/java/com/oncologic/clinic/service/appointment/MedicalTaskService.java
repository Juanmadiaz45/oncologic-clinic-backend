package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.entity.appointment.MedicalTask;

import java.util.List;

public interface MedicalTaskService {
    MedicalTask getMedicalTaskById(Long id);
    List<MedicalTask> getAllMedicalTasks();
    MedicalTask createMedicalTask(MedicalTask task);
    MedicalTask updateMedicalTask(MedicalTask task);
    void deleteMedicalTask(Long id);
}
