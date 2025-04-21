package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import com.oncologic.clinic.service.appointment.TypeOfMedicalAppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeOfMedicalAppointmentServiceImpl implements TypeOfMedicalAppointmentService {
    @Override
    public TypeOfMedicalAppointment getTypeOfMedicalAppointmentById(Long id) {
        return null;
    }

    @Override
    public List<TypeOfMedicalAppointment> getAllTypesOfMedicalAppointment() {
        return List.of();
    }

    @Override
    public TypeOfMedicalAppointment createTypeOfMedicalAppointment(TypeOfMedicalAppointment type) {
        return null;
    }

    @Override
    public TypeOfMedicalAppointment updateTypeOfMedicalAppointment(TypeOfMedicalAppointment type) {
        return null;
    }

    @Override
    public void deleteTypeOfMedicalAppointment(Long id) {

    }
}