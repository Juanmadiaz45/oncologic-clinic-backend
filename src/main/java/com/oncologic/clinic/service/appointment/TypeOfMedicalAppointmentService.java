package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;

import java.util.List;

public interface TypeOfMedicalAppointmentService {
    TypeOfMedicalAppointment getTypeOfMedicalAppointmentById(Long id);
    List<TypeOfMedicalAppointment> getAllTypesOfMedicalAppointment();
    TypeOfMedicalAppointment createTypeOfMedicalAppointment(TypeOfMedicalAppointment type);
    TypeOfMedicalAppointment updateTypeOfMedicalAppointment(TypeOfMedicalAppointment type);
    void deleteTypeOfMedicalAppointment(Long id);
}
