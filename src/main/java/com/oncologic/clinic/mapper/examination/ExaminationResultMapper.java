package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExaminationResultMapper {
    @Mapping(target = "medicalHistory", ignore = true)
    ExaminationResult toEntity(ExaminationResultRequestDTO dto);

    ExaminationResultResponseDTO toDto(ExaminationResult entity);

    @Mapping(target = "medicalHistory", ignore = true)
    void updateEntityFromDto(ExaminationResultUpdateDTO dto, @MappingTarget ExaminationResult entity);
}