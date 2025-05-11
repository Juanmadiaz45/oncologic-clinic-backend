package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdministrativeMapper {
    @Mapping(target = "idNumber", source = "dto.personalData.idNumber")
    @Mapping(target = "name", source = "dto.personalData.name")
    @Mapping(target = "lastName", source = "dto.personalData.lastName")
    Administrative toEntity(AdministrativeDTO dto);

    @Mapping(target = "personalData.userData.id", source = "entity.user.id")
    @Mapping(target = "personalData.userData.username", source = "entity.user.username")
    @Mapping(target = "personalData.idNumber", source = "entity.idNumber")
    @Mapping(target = "personalData.name", source = "entity.name")
    AdministrativeResponseDTO toDto(Administrative entity);
    void updateEntityFromDto(AdministrativeDTO dto, @MappingTarget Administrative entity);
}
