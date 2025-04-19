package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.PrescribedMedicine;

import java.util.List;

public interface PrescribedMedicineService {
    PrescribedMedicine getPrescribedMedicineById(Long id);
    List<PrescribedMedicine> getAllPrescribedMedicines();
    PrescribedMedicine prescribeMedicine(PrescribedMedicine medicine);
    PrescribedMedicine updatePrescribedMedicine(PrescribedMedicine medicine);
    void deletePrescribedMedicine(Long id);
}
