package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.exception.runtime.appointment.MedicalAppointmentNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalAppointmentMapper;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.service.appointment.MedicalAppointmentService;
import com.oncologic.clinic.service.appointment.MedicalOfficeService;
import com.oncologic.clinic.service.appointment.MedicalTaskService;
import com.oncologic.clinic.service.appointment.TypeOfMedicalAppointmentService;
import com.oncologic.clinic.service.patient.MedicalHistoryService;
import com.oncologic.clinic.service.patient.TreatmentService;
import com.oncologic.clinic.service.personal.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MedicalAppointmentServiceImpl implements MedicalAppointmentService {

    private final MedicalAppointmentRepository appointmentRepository;
    private final MedicalAppointmentMapper appointmentMapper;
    private final DoctorService doctorService;
    private final TypeOfMedicalAppointmentService typeService;
    private final MedicalHistoryService medicalHistoryService;
    private final TreatmentService treatmentService;
    private final MedicalOfficeService medicalOfficeService;
    private final MedicalTaskService medicalTaskService;

    @Override
    public MedicalAppointmentResponseDTO createMedicalAppointment(MedicalAppointmentDTO dto) {
        // Validaciones existentes...
        validateDoctor(dto.getDoctorId());
        validateTypeOfAppointment(dto.getTypeOfMedicalAppointmentId());
        validateMedicalHistory(dto.getMedicalHistoryId());

        if (dto.getTreatmentId() != null) {
            validateTreatment(dto.getTreatmentId());
        }

        // CAMBIO: Validar consultorio
        if (dto.getMedicalOfficeId() != null) {
            validateMedicalOffice(dto.getMedicalOfficeId());
        }

        MedicalAppointment appointment = appointmentMapper.toEntity(dto);

        // CAMBIO: Manejar consultorio
        if (dto.getMedicalOfficeId() != null) {
            MedicalOffice office = medicalOfficeService.getMedicalOfficeEntityById(dto.getMedicalOfficeId());
            appointment.setMedicalOffice(office);
        }

        // Manejar tareas médicas
        if (dto.getMedicalTaskIds() != null && !dto.getMedicalTaskIds().isEmpty()) {
            Set<MedicalTask> tasks = dto.getMedicalTaskIds().stream()
                    .map(medicalTaskService::getMedicalTaskEntityById)
                    .collect(Collectors.toSet());
            appointment.setMedicalTasks(tasks);
        }

        MedicalAppointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }

    @Override
    public MedicalAppointmentResponseDTO updateMedicalAppointment(Long id, MedicalAppointmentDTO dto) {
        MedicalAppointment existingAppointment = getMedicalAppointmentEntityById(id);

        // Validaciones si hay cambios...
        if (dto.getDoctorId() != null) {
            validateDoctor(dto.getDoctorId());
        }
        if (dto.getTypeOfMedicalAppointmentId() != null) {
            validateTypeOfAppointment(dto.getTypeOfMedicalAppointmentId());
        }
        if (dto.getMedicalHistoryId() != null) {
            validateMedicalHistory(dto.getMedicalHistoryId());
        }
        if (dto.getTreatmentId() != null) {
            validateTreatment(dto.getTreatmentId());
        }

        // CAMBIO: Validar consultorio
        if (dto.getMedicalOfficeId() != null) {
            validateMedicalOffice(dto.getMedicalOfficeId());
            MedicalOffice office = medicalOfficeService.getMedicalOfficeEntityById(dto.getMedicalOfficeId());
            existingAppointment.setMedicalOffice(office);
        }

        // Actualizar campos básicos
        appointmentMapper.updateFromDto(dto, existingAppointment);

        // Manejar tareas médicas
        if (dto.getMedicalTaskIds() != null) {
            Set<MedicalTask> tasks = dto.getMedicalTaskIds().stream()
                    .map(medicalTaskService::getMedicalTaskEntityById)
                    .collect(Collectors.toSet());
            existingAppointment.setMedicalTasks(tasks);
        }

        MedicalAppointment updatedAppointment = appointmentRepository.save(existingAppointment);
        return appointmentMapper.toDto(updatedAppointment);
    }

    private void validateMedicalOffice(Long officeId) {
        medicalOfficeService.getMedicalOfficeById(officeId); // Lanza excepción si no existe
    }

    // Métodos existentes sin cambios...
    private void validateDoctor(Long doctorId) {
        doctorService.getDoctorById(doctorId);
    }

    private void validateTypeOfAppointment(Long typeId) {
        typeService.getTypeOfMedicalAppointmentById(typeId);
    }

    private void validateMedicalHistory(Long historyId) {
        medicalHistoryService.getMedicalHistoryById(historyId);
    }

    private void validateTreatment(Long treatmentId) {
        treatmentService.getTreatmentById(treatmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalAppointmentResponseDTO getMedicalAppointmentById(Long id) {
        MedicalAppointment appointment = getMedicalAppointmentEntityById(id);
        return appointmentMapper.toDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalAppointmentResponseDTO> getAllMedicalAppointments() {
        return appointmentRepository.findAll().stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalAppointment getMedicalAppointmentEntityById(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(id));
    }

    @Override
    public void deleteMedicalAppointment(Long id) {
        MedicalAppointment appointment = getMedicalAppointmentEntityById(id);
        appointmentRepository.delete(appointment);
    }

    @Override
    public List<MedicalAppointmentResponseDTO> getBaseAppointments() {
        List<MedicalAppointment> baseAppointments = appointmentRepository.findBaseAppointments();
        return baseAppointments.stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<MedicalTaskResponseDTO> getAppointmentTasks(Long appointmentId) {
        MedicalAppointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        return appointment.getMedicalTasks().stream()
                .map(task -> MedicalTaskResponseDTO.builder()
                        .id(task.getId())
                        .description(task.getDescription())
                        .estimatedTime(task.getEstimatedTime())
                        .status(task.getStatus())
                        .responsible(task.getResponsible())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObservationResponseDTO> getAppointmentObservations(Long appointmentId) {
        MedicalAppointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        return appointment.getMedicalHistory().getAppointmentResults().stream()
                .flatMap(result -> result.getObservations().stream())
                .map(obs -> ObservationResponseDTO.builder()
                        .id(obs.getId())
                        .content(obs.getContent())
                        .recommendation(obs.getRecommendation())
                        .appointmentResultId(obs.getAppointmentResult().getId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TreatmentResponseDTO> getAppointmentTreatments(Long appointmentId) {
        MedicalAppointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        return appointment.getMedicalHistory().getAppointmentResults().stream()
                .flatMap(result -> result.getTreatments().stream())
                .map(treatment -> TreatmentResponseDTO.builder()
                        .id(treatment.getId())
                        .name(treatment.getName())
                        .description(treatment.getDescription())
                        .dateStart(treatment.getDateStart())
                        .endDate(treatment.getEndDate())
                        .appointmentResultId(treatment.getAppointmentResult().getId())
                        .typeOfTreatmentIds(treatment.getTypeOfTreatments().stream()
                                .map(type -> type.getId())
                                .collect(Collectors.toList()))
                        .prescribedMedicineIds(treatment.getPrescribedMedicines().stream()
                                .map(med -> med.getId())
                                .collect(Collectors.toList()))
                        .medicalAppointmentIds(treatment.getMedicalAppointments().stream()
                                .map(app -> app.getId())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getAppointmentDetails(Long appointmentId) {
        MedicalAppointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        Map<String, Object> details = new HashMap<>();
        details.put("id", appointment.getId());
        details.put("appointmentDate", appointment.getAppointmentDate());
        details.put("patientName", appointment.getMedicalHistory().getPatient().getName());
        details.put("patientId", appointment.getMedicalHistory().getPatient().getId());
        details.put("doctorName", appointment.getDoctor().getName() + " " + appointment.getDoctor().getLastName());
        details.put("doctorId", appointment.getDoctor().getId());
        details.put("officeName", appointment.getMedicalOffices().isEmpty() ? "No asignado" :
                appointment.getMedicalOffices().get(0).getName());
        details.put("appointmentType", appointment.getTypeOfMedicalAppointment().getName());
        details.put("status", determineAppointmentStatus(appointment));
        details.put("medicalHistoryId", appointment.getMedicalHistory().getId());

        return details;
    }

    @Override
    @Transactional
    public void startAppointment(Long appointmentId) {
        MedicalAppointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        // Actualizar estado de las tareas a "EN_PROGRESO" si están programadas
        appointment.getMedicalTasks().forEach(task -> {
            if ("PROGRAMADA".equals(task.getStatus())) {
                task.setStatus("EN_PROGRESO");
            }
        });

        repository.save(appointment);
    }

    @Override
    @Transactional
    public void completeAppointment(Long appointmentId) {
        MedicalAppointment appointment = repository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        // Verificar que todas las tareas estén completadas
        boolean allTasksCompleted = appointment.getMedicalTasks().stream()
                .allMatch(task -> "COMPLETADA".equals(task.getStatus()));

        if (!allTasksCompleted) {
            throw new IllegalStateException("No se puede completar la cita: hay tareas pendientes");
        }

        // Marcar como completada (esto podría ser un campo adicional en la entidad)
        log.info("Cita médica {} completada exitosamente", appointmentId);
    }

    private String determineAppointmentStatus(MedicalAppointment appointment) {
        if (appointment.getMedicalTasks().isEmpty()) {
            return "PROGRAMADA";
        }

        boolean allCompleted = appointment.getMedicalTasks().stream()
                .allMatch(task -> "COMPLETADA".equals(task.getStatus()));
        boolean anyInProgress = appointment.getMedicalTasks().stream()
                .anyMatch(task -> "EN_PROGRESO".equals(task.getStatus()));

        if (allCompleted) {
            return "COMPLETADA";
        } else if (anyInProgress) {
            return "EN_PROGRESO";
        } else {
            return "PROGRAMADA";
        }
    }
}

