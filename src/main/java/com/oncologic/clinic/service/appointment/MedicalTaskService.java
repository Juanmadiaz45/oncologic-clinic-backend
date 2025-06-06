package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.dto.appointment.MedicalTaskDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalTask;

import java.util.List;

public interface MedicalTaskService {
    MedicalTaskResponseDTO getMedicalTaskById(Long id);

    List<MedicalTaskResponseDTO> getAllMedicalTasks();

    MedicalTaskResponseDTO createMedicalTask(MedicalTaskDTO dto);

    MedicalTaskResponseDTO updateMedicalTask(Long id, MedicalTaskDTO dto);

    void deleteMedicalTask(Long id);

    MedicalTask getMedicalTaskEntityById(Long id);
}
