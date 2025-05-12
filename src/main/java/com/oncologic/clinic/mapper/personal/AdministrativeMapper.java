package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.personal.Personal;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PersonalMapper.class})
public interface AdministrativeMapper {
    @Mapping(target = "idNumber", source = "personalData.idNumber")
    @Mapping(target = "name", source = "personalData.name")
    @Mapping(target = "lastName", source = "personalData.lastName")
    @Mapping(target = "email", source = "personalData.email")
    @Mapping(target = "phoneNumber", source = "personalData.phoneNumber")
    @Mapping(target = "dateOfHiring", source = "personalData.dateOfHiring")
    @Mapping(target = "status", source = "personalData.status")
    Administrative toEntity(AdministrativeDTO dto);

    @Mapping(target = "personalData", source = ".", qualifiedByName = "mapPersonalData")
    AdministrativeResponseDTO toDto(Administrative entity);

    @Named("mapPersonalData")
    default PersonalResponseDTO mapPersonalData(Administrative administrative) {
        if (administrative == null) return null;
        return mapPersonal(administrative);
    }

    PersonalResponseDTO mapPersonal(Personal personal);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "idNumber", source = "personalData.idNumber")
    @Mapping(target = "name", source = "personalData.name")
    @Mapping(target = "lastName", source = "personalData.lastName")
    @Mapping(target = "email", source = "personalData.email")
    @Mapping(target = "phoneNumber", source = "personalData.phoneNumber")
    @Mapping(target = "dateOfHiring", source = "personalData.dateOfHiring")
    @Mapping(target = "status", source = "personalData.status")
    void updateEntityFromDto(AdministrativeDTO dto, @MappingTarget Administrative entity);
}
