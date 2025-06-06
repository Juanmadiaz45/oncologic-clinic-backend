package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
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

}

