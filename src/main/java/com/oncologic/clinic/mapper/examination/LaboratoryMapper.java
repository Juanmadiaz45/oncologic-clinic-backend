package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.LaboratoryDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LaboratoryMapper {
    Laboratory toEntity(LaboratoryDTO dto);
    LaboratoryResponseDTO toDto(Laboratory entity);
    void updateEntityFromDto(LaboratoryDTO dto, @MappingTarget Laboratory entity);
}