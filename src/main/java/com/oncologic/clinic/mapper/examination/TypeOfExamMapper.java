package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.request.TypeOfExamRequestDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.dto.examination.update.TypeOfExamUpdateDTO;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TypeOfExamMapper {
    TypeOfExam toEntity(TypeOfExamRequestDTO dto);
    TypeOfExamResponseDTO toDto(TypeOfExam entity);
    void updateEntityFromDto(TypeOfExamUpdateDTO dto, @MappingTarget TypeOfExam entity);
}