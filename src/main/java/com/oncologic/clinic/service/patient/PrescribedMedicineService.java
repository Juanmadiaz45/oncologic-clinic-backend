package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.PrescribedMedicineRequestDTO;
import com.oncologic.clinic.dto.patient.response.PrescribedMedicineResponseDTO;
import com.oncologic.clinic.dto.patient.update.PrescribedMedicineUpdateDTO;

import java.util.List;

public interface PrescribedMedicineService {
    PrescribedMedicineResponseDTO getPrescribedMedicineById(Long id);
    List<PrescribedMedicineResponseDTO> getAllPrescribedMedicines();
    PrescribedMedicineResponseDTO createPrescribedMedicine(PrescribedMedicineRequestDTO prescribedMedicineDTO);
    PrescribedMedicineResponseDTO updatePrescribedMedicine(Long id, PrescribedMedicineUpdateDTO prescribedMedicineDTO);
    void deletePrescribedMedicine(Long id);
}
