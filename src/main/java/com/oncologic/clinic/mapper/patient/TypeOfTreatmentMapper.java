package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.TypeOfTreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TypeOfTreatmentResponseDTO;
import com.oncologic.clinic.entity.patient.TypeOfTreatment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TypeOfTreatmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "treatment.id", source = "treatmentId")
    TypeOfTreatment toEntity(TypeOfTreatmentRequestDTO dto);

    @Mapping(target = "treatmentId", source = "treatment.id")
    TypeOfTreatmentResponseDTO toDto(TypeOfTreatment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "treatment", ignore = true)
    void updateEntityFromDto(TypeOfTreatmentRequestDTO dto, @MappingTarget TypeOfTreatment entity);
}
