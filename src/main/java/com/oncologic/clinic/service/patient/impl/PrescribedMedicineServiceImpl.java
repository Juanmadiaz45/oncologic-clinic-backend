package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.entity.patient.PrescribedMedicine;
import com.oncologic.clinic.service.patient.PrescribedMedicineService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescribedMedicineServiceImpl implements PrescribedMedicineService {
    @Override
    public PrescribedMedicine getPrescribedMedicineById(Long id) {
        return null;
    }

    @Override
    public List<PrescribedMedicine> getAllPrescribedMedicines() {
        return List.of();
    }

    @Override
    public PrescribedMedicine prescribeMedicine(PrescribedMedicine medicine) {
        return null;
    }

    @Override
    public PrescribedMedicine updatePrescribedMedicine(PrescribedMedicine medicine) {
        return null;
    }

    @Override
    public void deletePrescribedMedicine(Long id) {

    }
}