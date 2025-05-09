package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.TypeOfTreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TypeOfTreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TypeOfTreatmentUpdateDTO;
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
    @Mapping(target = "treatment.id", source = "treatmentId")
    void updateEntityFromDto(TypeOfTreatmentUpdateDTO dto, @MappingTarget TypeOfTreatment entity);
}
