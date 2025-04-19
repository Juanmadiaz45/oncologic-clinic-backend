package com.oncologic.clinic.service.appointment;


import com.oncologic.clinic.entity.appointment.MedicalOffice;

import java.util.List;

public interface MedicalOfficeService {
    MedicalOffice getMedicalOfficeById(Long id);
    List<MedicalOffice> getAllMedicalOffices();
    MedicalOffice createMedicalOffice(MedicalOffice office);
    MedicalOffice updateMedicalOffice(MedicalOffice office);
    void deleteMedicalOffice(Long id);
}
