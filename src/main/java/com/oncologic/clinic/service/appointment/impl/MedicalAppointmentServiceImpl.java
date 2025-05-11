package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.mapper.appointment.MedicalAppointmentMapper;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.appointment.MedicalOfficeRepository;
import com.oncologic.clinic.repository.appointment.MedicalTaskRepository;
import com.oncologic.clinic.repository.appointment.TypeOfMedicalAppointmentRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.service.appointment.MedicalAppointmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new EntityNotFoundException("Medical appointment not found"));
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
        MedicalAppointment appointment = mapper.toEntity(dto);

        appointment.setDoctor(doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("Doctor not found")));

        appointment.setTypeOfMedicalAppointment(typeRepository.findById(dto.getTypeOfMedicalAppointmentId())
                .orElseThrow(() -> new EntityNotFoundException("Type of appointment not found")));

        if(dto.getTreatmentId() != null) {
            appointment.setTreatment(treatmentRepository.findById(dto.getTreatmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Treatment not found")));
        }

        appointment.setMedicalHistory(medicalHistoryRepository.findById(dto.getMedicalHistoryId())
                .orElseThrow(() -> new EntityNotFoundException("Medical history not found")));

        MedicalAppointment savedAppointment = repository.save(appointment);

        if(dto.getMedicalTaskIds() != null && !dto.getMedicalTaskIds().isEmpty()) {
            Set<MedicalTask> tasks = new HashSet<>(medicalTaskRepository.findAllById(dto.getMedicalTaskIds()));
            savedAppointment.setMedicalTasks(tasks);
        }

        if(dto.getMedicalOfficeIds() != null && !dto.getMedicalOfficeIds().isEmpty()) {
            List<MedicalOffice> offices = medicalOfficeRepository.findAllById(dto.getMedicalOfficeIds());
            savedAppointment.setMedicalOffices(offices);
        }

        return mapper.toDto(repository.save(savedAppointment));
    }

    @Override
    @Transactional
    public MedicalAppointmentResponseDTO updateMedicalAppointment(Long id, MedicalAppointmentDTO dto) {
        MedicalAppointment existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical appointment not found"));

        mapper.updateFromDto(dto, existing);

        if(dto.getDoctorId() != null) {
            existing.setDoctor(doctorRepository.findById(dto.getDoctorId())
                    .orElseThrow(() -> new EntityNotFoundException("Doctor not found")));
        }

        if(dto.getTypeOfMedicalAppointmentId() != null) {
            existing.setTypeOfMedicalAppointment(typeRepository.findById(dto.getTypeOfMedicalAppointmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Type of appointment not found")));
        }

        return mapper.toDto(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteMedicalAppointment(Long id) {
        if(!repository.existsById(id)) {
            throw new EntityNotFoundException("Medical appointment not found");
        }
        repository.deleteById(id);
    }
}
