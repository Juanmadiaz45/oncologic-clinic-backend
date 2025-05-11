package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.TypeOfExamDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TypeOfExamMapper {
    TypeOfExam toEntity(TypeOfExamDTO dto);
    TypeOfExamResponseDTO toDto(TypeOfExam entity);
    void updateEntityFromDto(TypeOfExamDTO dto, @MappingTarget TypeOfExam entity);
}