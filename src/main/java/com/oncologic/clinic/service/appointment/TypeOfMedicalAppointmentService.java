package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.dto.appointment.TypeOfMedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.TypeOfMedicalAppointmentResponseDTO;

import java.util.List;

public interface TypeOfMedicalAppointmentService {
    TypeOfMedicalAppointmentResponseDTO getTypeOfMedicalAppointmentById(Long id);
    List<TypeOfMedicalAppointmentResponseDTO> getAllTypesOfMedicalAppointment();
    TypeOfMedicalAppointmentResponseDTO createTypeOfMedicalAppointment(TypeOfMedicalAppointmentDTO dto);
    TypeOfMedicalAppointmentResponseDTO updateTypeOfMedicalAppointment(Long id, TypeOfMedicalAppointmentDTO dto);
    void deleteTypeOfMedicalAppointment(Long id);
}
