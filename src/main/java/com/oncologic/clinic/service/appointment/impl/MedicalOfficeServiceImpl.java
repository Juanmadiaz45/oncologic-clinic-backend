package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.service.appointment.MedicalOfficeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalOfficeServiceImpl implements MedicalOfficeService {

    @Override
    public MedicalOffice getMedicalOfficeById(Long id) {
        return null;
    }

    @Override
    public List<MedicalOffice> getAllMedicalOffices() {
        return List.of();
    }

    @Override
    public MedicalOffice createMedicalOffice(MedicalOffice office) {
        return null;
    }

    @Override
    public MedicalOffice updateMedicalOffice(MedicalOffice office) {
        return null;
    }

    @Override
    public void deleteMedicalOffice(Long id) {

    }
}