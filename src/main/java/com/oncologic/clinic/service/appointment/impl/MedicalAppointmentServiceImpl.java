package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.exception.runtime.appointment.MedicalAppointmentNotFoundException;
import com.oncologic.clinic.exception.runtime.appointment.TypeOfMedicalAppointmentNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.MedicalHistoryNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.TreatmentNotFoundException;
import com.oncologic.clinic.exception.runtime.personal.DoctorNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalAppointmentMapper;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.appointment.MedicalOfficeRepository;
import com.oncologic.clinic.repository.appointment.MedicalTaskRepository;
import com.oncologic.clinic.repository.appointment.TypeOfMedicalAppointmentRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.service.appointment.MedicalAppointmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MedicalAppointmentServiceImpl implements MedicalAppointmentService {

    private final MedicalAppointmentRepository repository;
    private final DoctorRepository doctorRepository;
    private final TypeOfMedicalAppointmentRepository typeRepository;
    private final TreatmentRepository treatmentRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final MedicalTaskRepository medicalTaskRepository;
    private final MedicalOfficeRepository medicalOfficeRepository;
    private final MedicalAppointmentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public MedicalAppointmentResponseDTO getMedicalAppointmentById(Long id) {
        MedicalAppointment appointment = repository.findById(id)
                .orElseThrow(() -> new MedicalAppointmentNotFoundException(id));
        return mapper.toDto(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalAppointmentResponseDTO> getAllMedicalAppointments() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalAppointmentResponseDTO createMedicalAppointment(MedicalAppointmentDTO dto) {
        try {
            MedicalAppointment appointment = mapper.toEntity(dto);

            validateReferences(dto);

            MedicalAppointment savedAppointment = repository.save(appointment);

            return buildResponseDTO(savedAppointment, dto);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create medical appointment", e);
        }
    }

    private void validateReferences(MedicalAppointmentDTO dto) {
        if (!doctorRepository.existsById(dto.getDoctorId())) {
            throw new DoctorNotFoundException(dto.getDoctorId());
        }
        if (!typeRepository.existsById(dto.getTypeOfMedicalAppointmentId())) {
            throw new TypeOfMedicalAppointmentNotFoundException(dto.getTypeOfMedicalAppointmentId());
        }
        if (!medicalHistoryRepository.existsById(dto.getMedicalHistoryId())) {
            throw new MedicalHistoryNotFoundException(dto.getMedicalHistoryId());
        }
        if (dto.getTreatmentId() != null && !treatmentRepository.existsById(dto.getTreatmentId())) {
            throw new TreatmentNotFoundException(dto.getTreatmentId());
        }
    }

    private MedicalAppointmentResponseDTO buildResponseDTO(MedicalAppointment savedAppointment, MedicalAppointmentDTO originalDto) {
        return MedicalAppointmentResponseDTO.builder()
                .id(savedAppointment.getId())
                .doctorId(originalDto.getDoctorId())
                .typeOfMedicalAppointmentId(originalDto.getTypeOfMedicalAppointmentId())
                .appointmentDate(originalDto.getAppointmentDate())
                .treatmentId(originalDto.getTreatmentId())
                .medicalHistoryId(originalDto.getMedicalHistoryId())
                .medicalOfficeIds(originalDto.getMedicalOfficeIds())
                .medicalTaskIds(originalDto.getMedicalTaskIds())
                .build();
    }

    @Override
    @Transactional
    public MedicalAppointmentResponseDTO updateMedicalAppointment(Long id, MedicalAppointmentDTO dto) {
        try {
            log.info("Updating medical appointment with ID: {}", id);

            MedicalAppointment existing = repository.findById(id)
                    .orElseThrow(() -> new MedicalAppointmentNotFoundException(id));

            if (dto.getAppointmentDate() != null) {
                existing.setAppointmentDate(dto.getAppointmentDate());
            }

            if (dto.getDoctorId() != null) {
                if (!doctorRepository.existsById(dto.getDoctorId())) {
                    throw new DoctorNotFoundException(dto.getDoctorId());
                }
                Doctor doctor = new Doctor();
                doctor.setId(dto.getDoctorId());
                existing.setDoctor(doctor);
            }

            if (dto.getTypeOfMedicalAppointmentId() != null) {
                if (!typeRepository.existsById(dto.getTypeOfMedicalAppointmentId())) {
                    throw new TypeOfMedicalAppointmentNotFoundException(dto.getTypeOfMedicalAppointmentId());
                }
                TypeOfMedicalAppointment type = new TypeOfMedicalAppointment();
                type.setId(dto.getTypeOfMedicalAppointmentId());
                existing.setTypeOfMedicalAppointment(type);
            }

            if (dto.getTreatmentId() != null) {
                if (!treatmentRepository.existsById(dto.getTreatmentId())) {
                    throw new TreatmentNotFoundException(dto.getTreatmentId());
                }
                Treatment treatment = new Treatment();
                treatment.setId(dto.getTreatmentId());
                existing.setTreatment(treatment);
            }

            if (dto.getMedicalHistoryId() != null) {
                if (!medicalHistoryRepository.existsById(dto.getMedicalHistoryId())) {
                    throw new MedicalHistoryNotFoundException(dto.getMedicalHistoryId());
                }
                MedicalHistory medicalHistory = new MedicalHistory();
                medicalHistory.setId(dto.getMedicalHistoryId());
                existing.setMedicalHistory(medicalHistory);
            }

            MedicalAppointment savedAppointment = repository.save(existing);

            List<Long> finalOfficeIds = dto.getMedicalOfficeIds();
            if (dto.getMedicalOfficeIds() != null) {
                updateMedicalOffices(savedAppointment.getId(), dto.getMedicalOfficeIds());
            } else {
                finalOfficeIds = getCurrentOfficeIds(savedAppointment.getId());
            }

            Set<Long> finalTaskIds = dto.getMedicalTaskIds();
            if (dto.getMedicalTaskIds() != null) {
                validateMedicalTasks(dto.getMedicalTaskIds());
            } else {
                finalTaskIds = getCurrentTaskIds(savedAppointment.getId());
            }

            return MedicalAppointmentResponseDTO.builder()
                    .id(savedAppointment.getId())
                    .doctorId(dto.getDoctorId() != null ? dto.getDoctorId() : existing.getDoctor().getId())
                    .typeOfMedicalAppointmentId(dto.getTypeOfMedicalAppointmentId() != null ?
                            dto.getTypeOfMedicalAppointmentId() : existing.getTypeOfMedicalAppointment().getId())
                    .appointmentDate(savedAppointment.getAppointmentDate())
                    .treatmentId(dto.getTreatmentId() != null ? dto.getTreatmentId() :
                            (existing.getTreatment() != null ? existing.getTreatment().getId() : null))
                    .medicalHistoryId(dto.getMedicalHistoryId() != null ?
                            dto.getMedicalHistoryId() : existing.getMedicalHistory().getId())
                    .medicalOfficeIds(finalOfficeIds)
                    .medicalTaskIds(finalTaskIds)
                    .build();

        } catch (MedicalAppointmentNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating medical appointment with ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to update medical appointment", e);
        }
    }

    private void updateMedicalOffices(Long appointmentId, List<Long> officeIds) {
        medicalOfficeRepository.updateAppointmentForOffices(appointmentId, officeIds);
    }

    private void validateMedicalTasks(Set<Long> taskIds) {
        for (Long taskId : taskIds) {
            if (!medicalTaskRepository.existsById(taskId)) {
                throw new IllegalArgumentException("Medical task with ID " + taskId + " does not exist");
            }
        }
    }

    private List<Long> getCurrentOfficeIds(Long appointmentId) {
        return medicalOfficeRepository.findOfficeIdsByAppointmentId(appointmentId);
    }

    private Set<Long> getCurrentTaskIds(Long appointmentId) {
        return medicalTaskRepository.findTaskIdsByAppointmentId(appointmentId);
    }

    @Override
    @Transactional
    public void deleteMedicalAppointment(Long id) {
        if (!repository.existsById(id)) {
            throw new MedicalAppointmentNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
