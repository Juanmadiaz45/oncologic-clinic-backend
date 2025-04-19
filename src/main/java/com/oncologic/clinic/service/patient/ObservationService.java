package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.Observation;

import java.util.List;

public interface ObservationService {
    Observation getObservationById(Long id);
    List<Observation> getAllObservations();
    Observation createObservation(Observation observation);
    Observation updateObservation(Observation observation);
    void deleteObservation(Long id);

}
