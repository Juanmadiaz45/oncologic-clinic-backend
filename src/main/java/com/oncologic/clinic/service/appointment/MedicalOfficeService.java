package com.oncologic.clinic.service.appointment;


import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;

import java.util.List;

public interface MedicalOfficeService {
    MedicalOfficeResponseDTO getMedicalOfficeById(Long id);
    List<MedicalOfficeResponseDTO> getAllMedicalOffices();
    MedicalOfficeResponseDTO createMedicalOffice(MedicalOfficeDTO dto);
    MedicalOfficeResponseDTO updateMedicalOffice(Long id, MedicalOfficeDTO dto);
    void deleteMedicalOffice(Long id);
}
