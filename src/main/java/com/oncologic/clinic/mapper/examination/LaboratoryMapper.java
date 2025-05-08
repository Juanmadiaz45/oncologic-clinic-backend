package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.request.LaboratoryRequestDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;
import com.oncologic.clinic.dto.examination.update.LaboratoryUpdateDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LaboratoryMapper {
    Laboratory toEntity(LaboratoryRequestDTO dto);
    LaboratoryResponseDTO toDto(Laboratory entity);
    void updateEntityFromDto(LaboratoryUpdateDTO dto, @MappingTarget Laboratory entity);
}