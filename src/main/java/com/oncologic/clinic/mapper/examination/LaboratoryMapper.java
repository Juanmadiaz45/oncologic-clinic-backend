package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.LaboratoryDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface LaboratoryMapper {
    Laboratory toEntity(LaboratoryDTO dto);

    LaboratoryResponseDTO toDto(Laboratory entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(LaboratoryDTO dto, @MappingTarget Laboratory entity);
}