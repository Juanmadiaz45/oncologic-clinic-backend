package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.entity.patient.Observation;
import com.oncologic.clinic.service.patient.ObservationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObservationServiceImpl implements ObservationService {
    @Override
    public Observation getObservationById(Long id) {
        return null;
    }

    @Override
    public List<Observation> getAllObservations() {
        return List.of();
    }

    @Override
    public Observation createObservation(Observation observation) {
        return null;
    }

    @Override
    public Observation updateObservation(Observation observation) {
        return null;
    }

    @Override
    public void deleteObservation(Long id) {

    }
}