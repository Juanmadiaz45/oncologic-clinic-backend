package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExaminationResultMapper {
    @Mapping(target = "medicalHistory", ignore = true)
    ExaminationResult toEntity(ExaminationResultRequestDTO dto);

    @Mapping(source = "medicalHistory.id", target = "medicalHistoryId")
    ExaminationResultResponseDTO toDto(ExaminationResult entity);

    @Mapping(target = "medicalHistory", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ExaminationResultUpdateDTO dto, @MappingTarget ExaminationResult entity);
}