package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.PatientRequestDTO;
import com.oncologic.clinic.dto.patient.response.PatientResponseDTO;
import com.oncologic.clinic.dto.patient.update.PatientUpdateDTO;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.mapper.appointment.MedicalAppointmentMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {MedicalAppointmentMapper.class, MedicalHistoryMapper.class})
public interface PatientMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Patient toEntity(PatientRequestDTO dto);

    @Mapping(target = "userData", source = "user")
    @Mapping(target = "medicalHistory", source = "medicalHistory")
    PatientResponseDTO toDto(Patient entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "medicalHistory", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    void updateEntityFromDto(PatientUpdateDTO dto, @MappingTarget Patient entity);
}
