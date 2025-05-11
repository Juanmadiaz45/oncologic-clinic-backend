package com.oncologic.clinic.mapper.appointment;

import com.oncologic.clinic.dto.appointment.MedicalTaskDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalTaskMapper {

    @Mapping(target = "medicalAppointments", ignore = true)
    MedicalTask toEntity(MedicalTaskDTO dto);

    MedicalTaskResponseDTO toDto(MedicalTask entity);

    @Mapping(target = "medicalAppointments", ignore = true)
    void updateEntityFromDto(MedicalTaskDTO dto, @MappingTarget MedicalTask entity);
}
