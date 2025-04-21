package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.service.appointment.MedicalAppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeOfMedicalAppointmentServiceImpl implements MedicalAppointmentService {
    @Override
    public MedicalAppointment getMedicalAppointmentById(Long id) {
        return null;
    }

    @Override
    public List<MedicalAppointment> getAllMedicalAppointments() {
        return List.of();
    }

    @Override
    public MedicalAppointment createMedicalAppointment(MedicalAppointment appointment) {
        return null;
    }

    @Override
    public MedicalAppointment updateMedicalAppointment(MedicalAppointment appointment) {
        return null;
    }

    @Override
    public void deleteMedicalAppointment(Long id) {

    }
}