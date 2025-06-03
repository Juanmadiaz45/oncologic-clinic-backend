package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.DoctorDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.mapper.user.UserMapper;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface DoctorMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "specialities", ignore = true)
    @Mapping(target = "id", ignore = true)
    Doctor toEntity(DoctorDTO dto);

    @Mapping(target = "personalData", source = ".")
    @Mapping(target = "specialityIds", source = "specialities", qualifiedByName = "mapSpecialitiesToIds")
    DoctorResponseDTO toDto(Doctor entity);

    // Explicit mapping from Doctor to Personal Response DTO (same as Administrative)
    @Mapping(target = "userData", source = "user")
    @Mapping(target = "availabilityIds", source = "availabilities", qualifiedByName = "mapAvailabilitiesToIds")
    PersonalResponseDTO doctorToPersonalResponseDTO(Doctor doctor);

    @Named("mapAvailabilitiesToIds")
    default Set<Long> mapAvailabilitiesToIds(Set<Availability> availabilities) {
        if (availabilities == null || availabilities.isEmpty()) {
            return new HashSet<>();
        }
        return availabilities.stream()
                .map(Availability::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapSpecialitiesToIds")
    default Set<Long> mapSpecialitiesToIds(Set<Speciality> specialities) {
        if (specialities == null || specialities.isEmpty()) {
            return new HashSet<>();
        }
        return specialities.stream()
                .map(Speciality::getId)
                .collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "specialities", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(DoctorDTO dto, @MappingTarget Doctor entity);
}