package com.oncologic.clinic.mapper;

import com.oncologic.clinic.dto.personal.request.AdministrativeRequestDTO;
import com.oncologic.clinic.dto.personal.request.DoctorRequestDTO;
import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.dto.personal.update.AdministrativeUpdateDTO;
import com.oncologic.clinic.dto.personal.update.DoctorUpdateDTO;
import com.oncologic.clinic.dto.personal.update.PersonalUpdateDTO;
import com.oncologic.clinic.dto.personal.update.SpecialityUpdateDTO;
import com.oncologic.clinic.entity.personal.*;
import org.mapstruct.*;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonalMapper {

    Personal toEntity(PersonalRequestDTO dto);
    PersonalResponseDTO toDto(Personal entity);
    void updateEntityFromDto(PersonalUpdateDTO dto, @MappingTarget Personal entity);

    @Mapping(target = "specialities", ignore = true)
    Doctor toEntity(DoctorRequestDTO dto);
    DoctorResponseDTO toDto(Doctor entity);
    void updateEntityFromDto(DoctorUpdateDTO dto, @MappingTarget Doctor entity);

    Administrative toEntity(AdministrativeRequestDTO dto);
    AdministrativeResponseDTO toDto(Administrative entity);
    void updateEntityFromDto(AdministrativeUpdateDTO dto, @MappingTarget Administrative entity);

    Speciality toEntity(SpecialityRequestDTO dto);
    SpecialityResponseDTO toDto(Speciality entity);
    void updateEntityFromDto(SpecialityUpdateDTO dto, @MappingTarget Speciality entity);

    default Personal mapPersonalId(Long id) {
        if (id == null) return null;
        Personal personal = new Personal();
        personal.setId(id);
        return personal;
    }

    default Doctor mapDoctorId(Long id) {
        if (id == null) return null;
        Doctor doctor = new Doctor();
        doctor.setId(id);
        return doctor;
    }
}
