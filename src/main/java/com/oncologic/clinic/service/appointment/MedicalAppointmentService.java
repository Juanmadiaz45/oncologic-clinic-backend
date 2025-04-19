package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;

import java.util.List;

public interface MedicalAppointmentService {
    MedicalAppointment getMedicalAppointmentById(Long id);
    List<MedicalAppointment> getAllMedicalAppointments();
    MedicalAppointment createMedicalAppointment(MedicalAppointment appointment);
    MedicalAppointment updateMedicalAppointment(MedicalAppointment appointment);
    void deleteMedicalAppointment(Long id);
}
