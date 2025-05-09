package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.ObservationRequestDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.update.ObservationUpdateDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.Observation;
import com.oncologic.clinic.mapper.patient.ObservationMapper;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.ObservationRepository;
import com.oncologic.clinic.service.patient.ObservationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ObservationServiceImpl implements ObservationService {
    private final ObservationRepository observationRepository;
    private final AppointmentResultRepository appointmentResultRepository;
    private final ObservationMapper observationMapper;

    public ObservationServiceImpl(ObservationRepository observationRepository, AppointmentResultRepository appointmentResultRepository, ObservationMapper observationMapper) {
        this.observationRepository = observationRepository;
        this.appointmentResultRepository = appointmentResultRepository;
        this.observationMapper = observationMapper;
    }

    @Override
    public ObservationResponseDTO getObservationById(Long id) {
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Observación no encontrada con el ID: " + id));
        return observationMapper.toDto(observation);
    }

    @Override
    public List<ObservationResponseDTO> getAllObservations() {
        return observationRepository.findAll()
                .stream()
                .map(observationMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ObservationResponseDTO createObservation(ObservationRequestDTO observationDTO) {
        AppointmentResult appointmentResult = appointmentResultRepository.findById(observationDTO.getAppointmentResultId())
                .orElseThrow(() -> new EntityNotFoundException("Resultado de cita no encontrado con el ID: " + observationDTO.getAppointmentResultId()));

        Observation observation = observationMapper.toEntity(observationDTO);
        observation.setAppointmentResult(appointmentResult);

        Observation saved = observationRepository.save(observation);
        return observationMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ObservationResponseDTO updateObservation(Long id, ObservationUpdateDTO observationDTO) {
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Observación no encontrada con el ID: " + id));

        observationMapper.updateEntityFromDto(observationDTO, observation);
        Observation updated = observationRepository.save(observation);
        return observationMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteObservation(Long id) {
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Observación no encontrada con el ID: " + id));
        observationRepository.delete(observation);
    }
}