package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.dto.personal.update.SpecialityUpdateDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {

    @Mapping(target = "doctors", ignore = true)
    Speciality toEntity(SpecialityRequestDTO dto);

    @Mapping(target = "doctorIds", expression = "java(mapDoctorIds(entity.getDoctors()))")
    SpecialityResponseDTO toDto(Speciality entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(SpecialityUpdateDTO dto, @MappingTarget Speciality entity);

    default Set<Long> mapDoctorIds(Set<Doctor> doctors) {
        return doctors.stream()
                .map(Doctor::getId)
                .collect(Collectors.toSet());
    }
}
