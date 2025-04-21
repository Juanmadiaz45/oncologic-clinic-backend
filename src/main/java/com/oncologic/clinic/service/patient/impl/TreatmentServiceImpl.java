package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.service.patient.TreatmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentServiceImpl implements TreatmentService {
    @Override
    public Treatment getTreatmentById(Long id) {
        return null;
    }

    @Override
    public List<Treatment> getAllTreatments() {
        return List.of();
    }

    @Override
    public Treatment createTreatment(Treatment treatment) {
        return null;
    }

    @Override
    public Treatment updateTreatment(Treatment treatment) {
        return null;
    }

    @Override
    public void deleteTreatment(Long id) {

    }
}