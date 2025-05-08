package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.request.DoctorRequestDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.personal.update.DoctorUpdateDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DoctorMapper {
    @Mapping(target = "idNumber", source = "dto.personalData.idNumber")
    @Mapping(target = "name", source = "dto.personalData.name")
    @Mapping(target = "specialities", ignore = true)
    Doctor toEntity(DoctorRequestDTO dto);

    @Mapping(target = "personalData.userData.id", source = "entity.user.id")
    @Mapping(target = "personalData.idNumber", source = "entity.idNumber")
    @Mapping(target = "specialities", source = "entity.specialities")
    DoctorResponseDTO toDto(Doctor entity);

    void updateEntityFromDto(DoctorUpdateDTO dto, @MappingTarget Doctor entity);

    default Doctor mapDoctorId(Long id) {
        if (id == null) return null;
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }
}
