package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.SpecialityDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {

    @Mapping(target = "doctors", ignore = true)
    Speciality toEntity(SpecialityDTO dto);

    @Mapping(target = "doctorIds", source = "doctors", qualifiedByName = "mapDoctorsToIds")
    SpecialityResponseDTO toDto(Speciality entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "doctors", ignore = true)
    void updateEntityFromDto(SpecialityDTO dto, @MappingTarget Speciality entity);

    @Named("mapDoctorsToIds")
    default Set<Long> mapDoctorsToIds(Set<Doctor> doctors) {
        if (doctors == null || doctors.isEmpty()) {
            return new HashSet<>();
        }
        return doctors.stream()
                .map(Doctor::getId)
                .collect(Collectors.toSet());
    }
}