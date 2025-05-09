package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.MedicalHistoryRequestDTO;
import com.oncologic.clinic.dto.patient.response.MedicalHistoryResponseDTO;
import com.oncologic.clinic.dto.patient.update.MedicalHistoryUpdateDTO;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "medicalAppointments", ignore = true)
    @Mapping(target = "medicalExaminations", ignore = true)
    @Mapping(target = "appointmentResults", ignore = true)
    @Mapping(target = "examinationResults", ignore = true)
    MedicalHistory toEntity(MedicalHistoryRequestDTO dto);

    @Mapping(target = "patientId", source = "patient.id")
    MedicalHistoryResponseDTO toDto(MedicalHistory entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "currentHealthStatus", source = "dto.currentHealthStatus")
    void updateEntityFromDto(MedicalHistoryUpdateDTO dto, @MappingTarget MedicalHistory entity);
}
