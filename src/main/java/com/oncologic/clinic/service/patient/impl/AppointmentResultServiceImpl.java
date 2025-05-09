package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.AppointmentResultRequestDTO;
import com.oncologic.clinic.dto.patient.response.AppointmentResultResponseDTO;
import com.oncologic.clinic.dto.patient.update.AppointmentResultUpdateDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.mapper.patient.AppointmentResultMapper;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.patient.AppointmentResultService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppointmentResultServiceImpl implements AppointmentResultService {

    private final AppointmentResultRepository appointmentResultRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final AppointmentResultMapper appointmentResultMapper;

    public AppointmentResultServiceImpl(AppointmentResultRepository appointmentResultRepository, MedicalHistoryRepository medicalHistoryRepository, AppointmentResultMapper appointmentResultMapper) {
        this.appointmentResultRepository = appointmentResultRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.appointmentResultMapper = appointmentResultMapper;
    }

    @Override
    public AppointmentResultResponseDTO getAppointmentResultById(Long id) {
        AppointmentResult result = appointmentResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resultado de cita no encontrado con el ID: " + id));
        return appointmentResultMapper.toDto(result);
    }

    @Override
    public List<AppointmentResultResponseDTO> getAllAppointmentResults() {
        return appointmentResultRepository.findAll()
                .stream()
                .map(appointmentResultMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AppointmentResultResponseDTO createAppointmentResult(AppointmentResultRequestDTO dto) {
        MedicalHistory medicalHistory = medicalHistoryRepository.findById(dto.getMedicalHistoryId())
                .orElseThrow(() -> new EntityNotFoundException("Historial médico no encontrado con el ID: " + dto.getMedicalHistoryId()));

        AppointmentResult result = appointmentResultMapper.toEntity(dto);
        result.setMedicalHistory(medicalHistory);

        AppointmentResult saved = appointmentResultRepository.save(result);
        return appointmentResultMapper.toDto(saved);
    }

    @Override
    @Transactional
    public AppointmentResultResponseDTO updateAppointmentResult(Long id, AppointmentResultUpdateDTO dto) {
        AppointmentResult existing = appointmentResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resultado de cita no encontrado con el ID: " + id));

        appointmentResultMapper.updateEntityFromDto(dto, existing);
        AppointmentResult updated = appointmentResultRepository.save(existing);
        return appointmentResultMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteAppointmentResult(Long id) {
        AppointmentResult result = appointmentResultRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resultado de cita no encontrado con el ID: " + id));
        appointmentResultRepository.delete(result);
    }
}