package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { PersonalMapper.class })
public interface AdministrativeMapper {
    @Mapping(target = "idNumber", source = "personalData.idNumber")
    @Mapping(target = "name", source = "personalData.name")
    @Mapping(target = "lastName", source = "personalData.lastName")
    Administrative toEntity(AdministrativeDTO dto);

    @Mapping(target = "personalData", source = ".", qualifiedByName = "mapPersonalData")
    AdministrativeResponseDTO toDto(Administrative entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AdministrativeDTO dto, @MappingTarget Administrative entity);

    @Named("mapPersonalData")
    default PersonalResponseDTO mapPersonalData(Administrative admin) {
        if (admin == null) return null;

        return PersonalResponseDTO.builder()
                .id(admin.getId())
                .idNumber(admin.getIdNumber())
                .name(admin.getName())
                .lastName(admin.getLastName())
                .email(admin.getEmail())
                .phoneNumber(admin.getPhoneNumber())
                .dateOfHiring(admin.getDateOfHiring())
                .status(admin.getStatus())
                .userId(admin.getUser().getId())
                .build();
    }
}
