package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.DoctorDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {PersonalMapper.class})
public interface DoctorMapper {
    @Mapping(target = "idNumber", source = "dto.personalData.idNumber")
    @Mapping(target = "name", source = "dto.personalData.name")
    @Mapping(target = "specialities", ignore = true)
    Doctor toEntity(DoctorDTO dto);

    @Mapping(target = "personalData", source = ".", qualifiedByName = "mapPersonalData")
    DoctorResponseDTO toDto(Doctor entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(DoctorDTO dto, @MappingTarget Doctor entity);

    @Named("mapPersonalData")
    default PersonalResponseDTO mapPersonalData(Doctor doctor) {
        if (doctor == null) return null;

        return PersonalResponseDTO.builder()
                .id(doctor.getId())
                .idNumber(doctor.getIdNumber())
                .name(doctor.getName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .dateOfHiring(doctor.getDateOfHiring())
                .status(doctor.getStatus())
                .userId(doctor.getUser().getId())
                .build();
    }
}
