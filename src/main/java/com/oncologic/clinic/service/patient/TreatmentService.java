package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.Treatment;

import java.util.List;

public interface TreatmentService {
    Treatment getTreatmentById(Long id);
    List<Treatment> getAllTreatments();
    Treatment createTreatment(Treatment treatment);
    Treatment updateTreatment(Treatment treatment);
    void deleteTreatment(Long id);
}
