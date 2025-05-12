package com.oncologic.clinic.mapper.appointment;

import com.oncologic.clinic.dto.appointment.TypeOfMedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.TypeOfMedicalAppointmentResponseDTO;
import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TypeOfMedicalAppointmentMapper {

    TypeOfMedicalAppointment toEntity(TypeOfMedicalAppointmentDTO dto);

    TypeOfMedicalAppointmentResponseDTO toDto(TypeOfMedicalAppointment entity);

    void updateEntityFromDto(TypeOfMedicalAppointmentDTO dto, @MappingTarget TypeOfMedicalAppointment entity);
}
