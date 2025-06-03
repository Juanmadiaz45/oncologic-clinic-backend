package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalTaskDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.exception.runtime.appointment.MedicalTaskNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalTaskMapper;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.appointment.MedicalTaskRepository;
import com.oncologic.clinic.service.appointment.MedicalTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
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
                .orElseThrow(() -> new MedicalTaskNotFoundException(id));
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
        try {
            log.info("Creating medical task with description: {}", dto.getDescription());

            MedicalTask task = mapper.toEntity(dto);

            if (dto.getMedicalAppointmentIds() != null && !dto.getMedicalAppointmentIds().isEmpty()) {
                validateMedicalAppointments(dto.getMedicalAppointmentIds());
            }

            MedicalTask savedTask = repository.save(task);

            if (dto.getMedicalAppointmentIds() != null && !dto.getMedicalAppointmentIds().isEmpty()) {
                createTaskAppointmentRelations(savedTask.getId(), dto.getMedicalAppointmentIds());
            }

            return buildTaskResponseDTO(savedTask, dto);

        } catch (Exception e) {
            log.error("Error creating medical task: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create medical task", e);
        }
    }

    private void validateMedicalAppointments(Set<Long> appointmentIds) {
        for (Long appointmentId : appointmentIds) {
            if (!appointmentRepository.existsById(appointmentId)) {
                throw new IllegalArgumentException("Medical appointment with ID " + appointmentId + " does not exist");
            }
        }
    }

    private void createTaskAppointmentRelations(Long taskId, Set<Long> appointmentIds) {
        log.info("Task {} will be associated with {} appointments", taskId, appointmentIds.size());
    }

    private MedicalTaskResponseDTO buildTaskResponseDTO(MedicalTask savedTask, MedicalTaskDTO originalDto) {
        return MedicalTaskResponseDTO.builder()
                .id(savedTask.getId())
                .description(savedTask.getDescription())
                .estimatedTime(savedTask.getEstimatedTime())
                .status(savedTask.getStatus())
                .responsible(savedTask.getResponsible())
                .build();
    }

    @Override
    @Transactional
    public MedicalTaskResponseDTO updateMedicalTask(Long id, MedicalTaskDTO dto) {
        try {
            log.info("Updating medical task with ID: {}", id);

            MedicalTask existing = repository.findById(id)
                    .orElseThrow(() -> new MedicalTaskNotFoundException(id));

            if (dto.getDescription() != null) {
                existing.setDescription(dto.getDescription());
            }
            if (dto.getEstimatedTime() != null) {
                existing.setEstimatedTime(dto.getEstimatedTime());
            }
            if (dto.getStatus() != null) {
                existing.setStatus(dto.getStatus());
            }
            if (dto.getResponsible() != null) {
                existing.setResponsible(dto.getResponsible());
            }

            MedicalTask savedTask = repository.save(existing);

            return MedicalTaskResponseDTO.builder()
                    .id(savedTask.getId())
                    .description(savedTask.getDescription())
                    .estimatedTime(savedTask.getEstimatedTime())
                    .status(savedTask.getStatus())
                    .responsible(savedTask.getResponsible())
                    .build();

        } catch (MedicalTaskNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating medical task with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update medical task", e);
        }
    }

    @Override
    @Transactional
    public void deleteMedicalTask(Long id) {
        if (!repository.existsById(id)) {
            throw new MedicalTaskNotFoundException(id);
        }
        repository.deleteById(id);
    }
}