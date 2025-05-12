package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.AppointmentResultRequestDTO;
import com.oncologic.clinic.dto.patient.response.AppointmentResultResponseDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.exception.runtime.patient.AppointmentResultCreationException;
import com.oncologic.clinic.exception.runtime.patient.AppointmentResultNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.AppointmentResultUpdateException;
import com.oncologic.clinic.exception.runtime.patient.MedicalHistoryNotFoundException;
import com.oncologic.clinic.mapper.patient.AppointmentResultMapper;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.patient.AppointmentResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AppointmentResultServiceImpl implements AppointmentResultService {

    private final AppointmentResultRepository appointmentResultRepository;
    private final MedicalHistoryRepository medicalHistoryRepository;
    private final AppointmentResultMapper appointmentResultMapper;
    private static final Logger logger = LoggerFactory.getLogger(AppointmentResultServiceImpl.class);

    public AppointmentResultServiceImpl(AppointmentResultRepository appointmentResultRepository, MedicalHistoryRepository medicalHistoryRepository, AppointmentResultMapper appointmentResultMapper) {
        this.appointmentResultRepository = appointmentResultRepository;
        this.medicalHistoryRepository = medicalHistoryRepository;
        this.appointmentResultMapper = appointmentResultMapper;
    }

    @Override
    public AppointmentResultResponseDTO getAppointmentResultById(Long id) {
        try {
            AppointmentResult result = appointmentResultRepository.findById(id)
                    .orElseThrow(() -> new AppointmentResultNotFoundException(id));
            return appointmentResultMapper.toDto(result);

        } catch (AppointmentResultNotFoundException ex) {
            logger.warn("Intento de acceso a resultado de cita no existente: ID {}", id);
            throw ex;

        } catch (Exception ex) {
            logger.error("Error inesperado al obtener resultado de cita con ID {}", id, ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving appointment result", ex);
        }
    }

    @Override
    public List<AppointmentResultResponseDTO> getAllAppointmentResults() {
        try {
            return appointmentResultRepository.findAll()
                    .stream()
                    .map(appointmentResultMapper::toDto)
                    .toList();

        } catch (Exception ex) {
            logger.error("Error al recuperar todos los resultados de cita", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving appointment results", ex);
        }
    }

    @Override
    @Transactional
    public AppointmentResultResponseDTO createAppointmentResult(AppointmentResultRequestDTO dto) {
        try {
            MedicalHistory medicalHistory = medicalHistoryRepository.findById(dto.getMedicalHistoryId())
                    .orElseThrow(() -> new MedicalHistoryNotFoundException(dto.getMedicalHistoryId()));

            AppointmentResult result = appointmentResultMapper.toEntity(dto);
            result.setMedicalHistory(medicalHistory);

            AppointmentResult saved = appointmentResultRepository.save(result);
            logger.info("Resultado de cita creado exitosamente con ID: {}", saved.getId());

            return appointmentResultMapper.toDto(saved);

        } catch (MedicalHistoryNotFoundException ex) {
            logger.warn("Intento de crear resultado de cita con historial médico no existente: ID {}",
                    dto.getMedicalHistoryId());
            throw ex;

        } catch (DataIntegrityViolationException ex) {
            String errorMsg = "Data integrity error while creating citation result: " +
                    ex.getMostSpecificCause().getMessage();
            logger.error(errorMsg, ex);
            throw new AppointmentResultCreationException(errorMsg, ex);

        } catch (Exception ex) {
            logger.error("Error inesperado al crear resultado de cita", ex);
            throw new AppointmentResultCreationException("Error creating appointment result", ex);
        }
    }

    @Override
    @Transactional
    public AppointmentResultResponseDTO updateAppointmentResult(Long id, AppointmentResultRequestDTO dto) {
        try {
            AppointmentResult existing = appointmentResultRepository.findById(id)
                    .orElseThrow(() -> new AppointmentResultNotFoundException(id));

            appointmentResultMapper.updateEntityFromDto(dto, existing);

            if (dto.getMedicalHistoryId() != null) {
                MedicalHistory medicalHistory = medicalHistoryRepository.findById(dto.getMedicalHistoryId())
                        .orElseThrow(() -> new MedicalHistoryNotFoundException(dto.getMedicalHistoryId()));
                existing.setMedicalHistory(medicalHistory);
            }

            AppointmentResult updated = appointmentResultRepository.save(existing);
            logger.info("Resultado de cita actualizado exitosamente con ID: {}", id);

            return appointmentResultMapper.toDto(updated);

        } catch (AppointmentResultNotFoundException | MedicalHistoryNotFoundException ex) {
            logger.warn("Intento de actualización con recurso no encontrado: {}", ex.getMessage());
            throw ex;

        } catch (DataIntegrityViolationException ex) {
            String errorMsg = "Data integrity error when updating appointment result: " +
                    ex.getMostSpecificCause().getMessage();
            logger.error(errorMsg, ex);
            throw new AppointmentResultUpdateException(errorMsg, ex);

        } catch (Exception ex) {
            logger.error("Error inesperado al actualizar resultado de cita con ID {}", id, ex);
            throw new AppointmentResultUpdateException("Error updating appointment result", ex);
        }
    }

    @Override
    @Transactional
    public void deleteAppointmentResult(Long id) {
        try {
            AppointmentResult result = appointmentResultRepository.findById(id)
                    .orElseThrow(() -> new AppointmentResultNotFoundException(id));

            appointmentResultRepository.delete(result);

        } catch (AppointmentResultNotFoundException ex) {
            throw ex;

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Error deleting quote result: " + e.getMostSpecificCause().getMessage(), e);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unexpected error when deleting the appointment result", e);
        }
    }
}