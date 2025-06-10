package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExaminationResultMapper {
    @Mapping(target = "medicalHistory", ignore = true)
    @Mapping(target = "resultsReport", ignore = true) // Se maneja en el servicio
    ExaminationResult toEntity(ExaminationResultRequestDTO dto);

    @Mapping(source = "medicalHistory.id", target = "medicalHistoryId")
    @Mapping(source = "resultsReport", target = "reportContent")
    @Mapping(target = "hasReport", expression = "java(entity.getResultsReport() != null && !entity.getResultsReport().trim().isEmpty())")
    @Mapping(target = "reportSizeBytes", expression = "java(entity.getResultsReport() != null ? entity.getResultsReport().length() : 0)")
    @Mapping(target = "reportFormat", constant = "text/plain")
    ExaminationResultResponseDTO toDto(ExaminationResult entity);

    @Mapping(target = "medicalHistory", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ExaminationResultUpdateDTO dto, @MappingTarget ExaminationResult entity);
}