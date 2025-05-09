package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.PatientRequestDTO;
import com.oncologic.clinic.dto.patient.response.PatientResponseDTO;
import com.oncologic.clinic.dto.patient.update.PatientUpdateDTO;
import com.oncologic.clinic.entity.patient.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patient toEntity(PatientRequestDTO dto);
    PatientResponseDTO toDto(Patient entity);
    void updateEntityFromDto(PatientUpdateDTO dto, @MappingTarget Patient entity);
}
