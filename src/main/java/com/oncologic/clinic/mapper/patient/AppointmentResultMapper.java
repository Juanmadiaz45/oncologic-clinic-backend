package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.AppointmentResultRequestDTO;
import com.oncologic.clinic.dto.patient.response.AppointmentResultResponseDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import org.mapstruct.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AppointmentResultMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    @Mapping(target = "observations", ignore = true)
    @Mapping(target = "treatments", ignore = true)
    AppointmentResult toEntity(AppointmentResultRequestDTO dto);

    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    @Mapping(
            target = "observationIds",
            expression = "java(entity.getObservations().stream().map(o -> o.getId()).collect(java.util.stream.Collectors.toList()))"
    )
    @Mapping(
            target = "treatmentIds",
            expression = "java(entity.getTreatments().stream().map(t -> t.getId()).collect(java.util.stream.Collectors.toList()))"
    )
    AppointmentResultResponseDTO toDto(AppointmentResult entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "medicalHistory", ignore = true)
    @Mapping(target = "observations", ignore = true)
    @Mapping(target = "treatments", ignore = true)
    void updateEntityFromDto(AppointmentResultRequestDTO dto, @MappingTarget AppointmentResult entity);
}
