package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.exception.runtime.appointment.MedicalAppointmentNotFoundException;
import com.oncologic.clinic.exception.runtime.appointment.MedicalTaskNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalAppointmentMapper;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.appointment.MedicalTaskRepository;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.PrescribedMedicineRepository;
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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MedicalAppointmentServiceImpl implements MedicalAppointmentService {

    private final MedicalAppointmentRepository appointmentRepository;
    private final MedicalAppointmentMapper appointmentMapper;
    private final AppointmentResultRepository appointmentResultRepository;
    private final DoctorService doctorService;
    private final TypeOfMedicalAppointmentService typeService;
    private final MedicalHistoryService medicalHistoryService;
    private final TreatmentService treatmentService;
    private final MedicalOfficeService medicalOfficeService;
    private final MedicalTaskService medicalTaskService;
    private final PrescribedMedicineRepository prescribedMedicineRepository;
    private final MedicalTaskRepository medicalTaskRepository;

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
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
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
        MedicalAppointment appointment = appointmentRepository.findByIdWithDetails(appointmentId);
        if (appointment == null) {
            throw new MedicalAppointmentNotFoundException(appointmentId);
        }

        // Buscar o crear AppointmentResult
        List<AppointmentResult> results = appointment.getMedicalHistory().getAppointmentResults();

        return results.stream()
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
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
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
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        Map<String, Object> details = new HashMap<>();
        details.put("id", appointment.getId());
        details.put("appointmentDate", appointment.getAppointmentDate());
        details.put("patientName", appointment.getMedicalHistory().getPatient().getName());
        details.put("patientId", appointment.getMedicalHistory().getPatient().getId());
        details.put("doctorName", appointment.getDoctor().getName() + " " + appointment.getDoctor().getLastName());
        details.put("doctorId", appointment.getDoctor().getId());

        String officeName = appointment.getMedicalOffice() != null ?
                appointment.getMedicalOffice().getName() : "No asignado";
        details.put("officeName", officeName);

        details.put("appointmentType", appointment.getTypeOfMedicalAppointment().getName());
        details.put("status", determineAppointmentStatus(appointment));
        details.put("medicalHistoryId", appointment.getMedicalHistory().getId());

        return details;
    }

    @Override
    @Transactional
    public void startAppointment(Long appointmentId) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        // CAMBIO: Usar "PENDIENTE" en lugar de "PROGRAMADA"
        appointment.getMedicalTasks().forEach(task -> {
            if ("PENDIENTE".equals(task.getStatus())) {
                task.setStatus("EN_PROGRESO");
            }
        });

        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void completeAppointment(Long appointmentId) {
        MedicalAppointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(appointmentId));

        // CAMBIO: Usar "COMPLETADA" (mantener igual si es correcto en BD)
        boolean allTasksCompleted = appointment.getMedicalTasks().stream()
                .allMatch(task -> "COMPLETADA".equals(task.getStatus()));

        if (!allTasksCompleted) {
            throw new IllegalStateException("No se puede completar la cita: hay tareas pendientes");
        }

        ensureAppointmentResultExists(appointment);

        log.info("Cita médica {} completada exitosamente", appointmentId);
    }

    private void ensureAppointmentResultExists(MedicalAppointment appointment) {
        boolean hasResult = appointment.getMedicalHistory().getAppointmentResults().stream()
                .anyMatch(result -> result.getEvaluationDate().toLocalDate()
                        .equals(appointment.getAppointmentDate().toLocalDate()));

        if (!hasResult) {
            AppointmentResult result = new AppointmentResult();
            result.setEvaluationDate(LocalDateTime.now());
            result.setMedicalHistory(appointment.getMedicalHistory());
            appointmentResultRepository.save(result);
        }
    }

    private String determineAppointmentStatus(MedicalAppointment appointment) {
        if (appointment.getMedicalTasks().isEmpty()) {
            return "PENDIENTE"; // CAMBIO: Usar "PENDIENTE" en lugar de "PROGRAMADA"
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
            return "PENDIENTE";
        }
    }

    @Override
    @Transactional
    public Map<String, Object> getOrCreateAppointmentResult(Long appointmentId) {
        MedicalAppointment appointment = appointmentRepository.findByIdWithDetails(appointmentId);
        if (appointment == null) {
            throw new MedicalAppointmentNotFoundException(appointmentId);
        }

        // Buscar AppointmentResult existente para hoy
        LocalDateTime today = LocalDateTime.now();
        AppointmentResult result = appointment.getMedicalHistory().getAppointmentResults().stream()
                .filter(ar -> ar.getEvaluationDate().toLocalDate().equals(today.toLocalDate()))
                .findFirst()
                .orElse(null);

        // Si no existe, crear uno nuevo
        if (result == null) {
            result = new AppointmentResult();
            result.setEvaluationDate(today);
            result.setMedicalHistory(appointment.getMedicalHistory());
            result = appointmentResultRepository.save(result);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", result.getId());
        response.put("evaluationDate", result.getEvaluationDate());
        response.put("medicalHistoryId", result.getMedicalHistory().getId());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTreatmentMedicines(Long treatmentId) {
        return prescribedMedicineRepository.findByTreatmentId(treatmentId).stream()
                .map(medicine -> {
                    Map<String, Object> medicineMap = new HashMap<>();
                    medicineMap.put("id", medicine.getId());
                    medicineMap.put("medicine", medicine.getMedicine());
                    medicineMap.put("dose", medicine.getDose());
                    medicineMap.put("instructions", medicine.getInstructions());
                    medicineMap.put("duration", medicine.getDuration());
                    medicineMap.put("frequencyOfAdministration", medicine.getFrequencyOfAdministration());
                    medicineMap.put("prescriptionDate", medicine.getPrescriptionDate());
                    return medicineMap;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalTask getMedicalTaskEntityById(Long taskId) {
        return medicalTaskRepository.findById(taskId)
                .orElseThrow(() -> new MedicalTaskNotFoundException(taskId));
    }
}

