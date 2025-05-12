package com.oncologic.clinic.mapper.appointment;

import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicalOfficeMapper {

    @Mapping(target = "medicalAppointment", ignore = true)
    MedicalOffice toEntity(MedicalOfficeDTO dto);

    MedicalOfficeResponseDTO toDto(MedicalOffice entity);

    @Mapping(target = "medicalAppointment", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(MedicalOfficeDTO dto, @MappingTarget MedicalOffice entity);
}