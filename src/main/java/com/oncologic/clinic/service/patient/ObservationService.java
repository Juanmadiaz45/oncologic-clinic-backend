package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.ObservationRequestDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.update.ObservationUpdateDTO;

import java.util.List;

public interface ObservationService {
    ObservationResponseDTO getObservationById(Long id);
    List<ObservationResponseDTO> getAllObservations();
    ObservationResponseDTO createObservation(ObservationRequestDTO observationDTO);
    ObservationResponseDTO updateObservation(Long id, ObservationUpdateDTO observationDTO);
    void deleteObservation(Long id);
}
