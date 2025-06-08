package com.oncologic.clinic.service.appointment;


import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalOffice;

import java.util.List;

public interface MedicalOfficeService {
    MedicalOfficeResponseDTO getMedicalOfficeById(Long id);

    List<MedicalOfficeResponseDTO> getAllMedicalOffices();

    MedicalOfficeResponseDTO createMedicalOffice(MedicalOfficeDTO dto);

    MedicalOfficeResponseDTO updateMedicalOffice(Long id, MedicalOfficeDTO dto);

    void deleteMedicalOffice(Long id);

    MedicalOffice getMedicalOfficeEntityById(Long id);

    List<MedicalOfficeResponseDTO> getAvailableOffices(String date, String startTime, String endTime);
}
