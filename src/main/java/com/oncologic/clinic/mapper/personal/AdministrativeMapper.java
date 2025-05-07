package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.request.AdministrativeRequestDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.update.AdministrativeUpdateDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdministrativeMapper {
    Administrative toEntity(AdministrativeRequestDTO dto);
    AdministrativeResponseDTO toDto(Administrative entity);
    void updateEntityFromDto(AdministrativeUpdateDTO dto, @MappingTarget Administrative entity);
}
