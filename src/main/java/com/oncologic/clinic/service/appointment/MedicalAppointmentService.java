package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;

import java.util.List;

public interface MedicalAppointmentService {
    MedicalAppointmentResponseDTO getMedicalAppointmentById(Long id);
    List<MedicalAppointmentResponseDTO> getAllMedicalAppointments();
    MedicalAppointmentResponseDTO createMedicalAppointment(MedicalAppointmentDTO dto);
    MedicalAppointmentResponseDTO updateMedicalAppointment(Long id, MedicalAppointmentDTO dto);
    void deleteMedicalAppointment(Long id);
}
