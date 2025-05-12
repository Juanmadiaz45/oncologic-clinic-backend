package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.TypeOfExamDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TypeOfExamMapper {
    TypeOfExam toEntity(TypeOfExamDTO dto);

    TypeOfExamResponseDTO toDto(TypeOfExam entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TypeOfExamDTO dto, @MappingTarget TypeOfExam entity);
}