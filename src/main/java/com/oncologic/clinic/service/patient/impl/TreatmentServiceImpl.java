package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.TreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TreatmentUpdateDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.mapper.patient.TreatmentMapper;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.service.patient.TreatmentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TreatmentServiceImpl implements TreatmentService {
    private final TreatmentRepository treatmentRepository;
    private final AppointmentResultRepository appointmentResultRepository;
    private final TreatmentMapper treatmentMapper;

    public TreatmentServiceImpl(TreatmentRepository treatmentRepository, AppointmentResultRepository appointmentResultRepository, TreatmentMapper treatmentMapper) {
        this.treatmentRepository = treatmentRepository;
        this.appointmentResultRepository = appointmentResultRepository;
        this.treatmentMapper = treatmentMapper;
    }

    @Override
    public TreatmentResponseDTO getTreatmentById(Long id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado con el ID: " + id));
        return treatmentMapper.toDto(treatment);
    }

    @Override
    public List<TreatmentResponseDTO> getAllTreatments() {
        return treatmentRepository.findAll()
                .stream()
                .map(treatmentMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public TreatmentResponseDTO createTreatment(TreatmentRequestDTO treatmentDTO) {
        AppointmentResult appointmentResult = appointmentResultRepository.findById(treatmentDTO.getAppointmentResultId())
                .orElseThrow(() -> new EntityNotFoundException("Resultado de cita no encontrado con el ID: " + treatmentDTO.getAppointmentResultId()));

        Treatment treatment = treatmentMapper.toEntity(treatmentDTO);
        treatment.setAppointmentResult(appointmentResult);

        Treatment saved = treatmentRepository.save(treatment);
        return treatmentMapper.toDto(saved);
    }

    @Override
    @Transactional
    public TreatmentResponseDTO updateTreatment(Long id, TreatmentUpdateDTO treatmentDTO) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado con el ID: " + id));

        treatmentMapper.updateEntityFromDto(treatmentDTO, treatment);
        Treatment updated = treatmentRepository.save(treatment);
        return treatmentMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteTreatment(Long id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado con el ID: " + id));
        treatmentRepository.delete(treatment);
    }
}