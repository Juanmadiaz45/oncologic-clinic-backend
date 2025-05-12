package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalTaskDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.mapper.appointment.MedicalTaskMapper;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.appointment.MedicalTaskRepository;
import com.oncologic.clinic.service.appointment.MedicalTaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalTaskServiceImpl implements MedicalTaskService {

    private final MedicalTaskRepository repository;
    private final MedicalAppointmentRepository appointmentRepository;
    private final MedicalTaskMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public MedicalTaskResponseDTO getMedicalTaskById(Long id) {
        MedicalTask task = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical task not found"));
        return mapper.toDto(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalTaskResponseDTO> getAllMedicalTasks() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalTaskResponseDTO createMedicalTask(MedicalTaskDTO dto) {
        MedicalTask task = mapper.toEntity(dto);

        if(dto.getMedicalAppointmentIds() != null && !dto.getMedicalAppointmentIds().isEmpty()) {
            Set<MedicalAppointment> appointments = new HashSet<>(appointmentRepository.findAllById(dto.getMedicalAppointmentIds()));
            task.setMedicalAppointments(appointments);
        }

        return mapper.toDto(repository.save(task));
    }

    @Override
    @Transactional
    public MedicalTaskResponseDTO updateMedicalTask(Long id, MedicalTaskDTO dto) {
        MedicalTask existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical task not found"));

        mapper.updateEntityFromDto(dto, existing);

        if(dto.getMedicalAppointmentIds() != null) {
            Set<MedicalAppointment> appointments = new HashSet<>(appointmentRepository.findAllById(dto.getMedicalAppointmentIds()));
            existing.setMedicalAppointments(appointments);
        }

        return mapper.toDto(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteMedicalTask(Long id) {
        if(!repository.existsById(id)) {
            throw new EntityNotFoundException("Medical task not found");
        }
        repository.deleteById(id);
    }
}