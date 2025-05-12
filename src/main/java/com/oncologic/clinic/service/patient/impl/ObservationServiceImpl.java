package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.ObservationRequestDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.update.ObservationUpdateDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.Observation;
import com.oncologic.clinic.exception.runtime.patient.AppointmentResultNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.ObservationNotFoundException;
import com.oncologic.clinic.mapper.patient.ObservationMapper;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.ObservationRepository;
import com.oncologic.clinic.service.patient.ObservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ObservationServiceImpl implements ObservationService {
    private static final Logger logger = LoggerFactory.getLogger(ObservationServiceImpl.class);

    private final ObservationRepository observationRepository;
    private final AppointmentResultRepository appointmentResultRepository;
    private final ObservationMapper observationMapper;

    public ObservationServiceImpl(ObservationRepository observationRepository,
                                  AppointmentResultRepository appointmentResultRepository,
                                  ObservationMapper observationMapper) {
        this.observationRepository = observationRepository;
        this.appointmentResultRepository = appointmentResultRepository;
        this.observationMapper = observationMapper;
    }

    @Override
    public ObservationResponseDTO getObservationById(Long id) {
        logger.info("Fetching observation with ID: {}", id);
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Observation not found with ID: {}", id);
                    return new ObservationNotFoundException(id);
                });
        return observationMapper.toDto(observation);
    }

    @Override
    public List<ObservationResponseDTO> getAllObservations() {
        logger.info("Fetching all observations");
        List<ObservationResponseDTO> observations = observationRepository.findAll()
                .stream()
                .map(observationMapper::toDto)
                .toList();

        if (observations.isEmpty()) {
            logger.info("No observations found in database");
        }
        return observations;
    }

    @Override
    @Transactional
    public ObservationResponseDTO createObservation(ObservationRequestDTO observationDTO) {
        logger.info("Creating new observation for appointment result ID: {}", observationDTO.getAppointmentResultId());

        try {
            AppointmentResult appointmentResult = appointmentResultRepository
                    .findById(observationDTO.getAppointmentResultId())
                    .orElseThrow(() -> {
                        logger.warn("Appointment result not found with ID: {}", observationDTO.getAppointmentResultId());
                        return new AppointmentResultNotFoundException(observationDTO.getAppointmentResultId());
                    });

            Observation observation = observationMapper.toEntity(observationDTO);
            observation.setAppointmentResult(appointmentResult);

            Observation saved = observationRepository.save(observation);
            logger.info("Successfully created observation with ID: {}", saved.getId());
            return observationMapper.toDto(saved);

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while creating observation: {}", e.getMessage());
            throw new DataIntegrityViolationException("Failed to create observation due to data integrity violation", e);
        }
    }

    @Override
    @Transactional
    public ObservationResponseDTO updateObservation(Long id, ObservationUpdateDTO observationDTO) {
        logger.info("Updating observation with ID: {}", id);

        try {
            Observation observation = observationRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Observation not found with ID: {}", id);
                        return new ObservationNotFoundException(id);
                    });

            observationMapper.updateEntityFromDto(observationDTO, observation);
            Observation updated = observationRepository.save(observation);
            logger.info("Successfully updated observation with ID: {}", id);
            return observationMapper.toDto(updated);

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while updating observation: {}", e.getMessage());
            throw new DataIntegrityViolationException("Failed to update observation due to data integrity violation", e);
        }
    }

    @Override
    @Transactional
    public void deleteObservation(Long id) {
        logger.info("Deleting observation with ID: {}", id);

        try {
            Observation observation = observationRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Observation not found with ID: {}", id);
                        return new ObservationNotFoundException(id);
                    });

            observationRepository.delete(observation);
            logger.info("Successfully deleted observation with ID: {}", id);

        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation while deleting observation: {}", e.getMessage());
            throw new DataIntegrityViolationException(
                    "Cannot delete observation because it has associated records", e);
        }
    }
}