package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.ObservationRequestDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.update.ObservationUpdateDTO;
import com.oncologic.clinic.entity.patient.Observation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ObservationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointmentResult.id", source = "dto.appointmentResultId")
    Observation toEntity(ObservationRequestDTO dto);

    @Mapping(target = "appointmentResultId", source = "entity.appointmentResult.id")
    ObservationResponseDTO toDto(Observation entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointmentResult", ignore = true)
    void updateEntityFromDto(ObservationUpdateDTO dto, @MappingTarget Observation entity);
}
