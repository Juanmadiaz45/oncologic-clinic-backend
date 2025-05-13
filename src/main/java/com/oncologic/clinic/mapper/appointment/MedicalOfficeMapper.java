package com.oncologic.clinic.mapper.appointment;

import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicalOfficeMapper {
    MedicalOffice toEntity(MedicalOfficeDTO dto);

    @Mapping(target = "medicalAppointmentId", source = "medicalAppointment.id")
    MedicalOfficeResponseDTO toDto(MedicalOffice entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MedicalOfficeDTO dto, @MappingTarget MedicalOffice entity);
}