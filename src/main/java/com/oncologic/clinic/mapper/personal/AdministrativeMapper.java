package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.mapper.user.UserMapper;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AdministrativeMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "id", ignore = true)
    Administrative toEntity(AdministrativeDTO dto);

    @Mapping(target = "personalData", source = ".")
    AdministrativeResponseDTO toDto(Administrative entity);

    // Explicit mapping from Administrative to PersonalResponseDTO
    @Mapping(target = "userData", source = "user")
    @Mapping(target = "availabilityIds", source = "availabilities", qualifiedByName = "mapAvailabilitiesToIds")
    PersonalResponseDTO administrativeToPersonalResponseDTO(Administrative administrative);

    @Named("mapAvailabilitiesToIds")
    default Set<Long> mapAvailabilitiesToIds(Set<Availability> availabilities) {
        if (availabilities == null || availabilities.isEmpty()) {
            return new HashSet<>();
        }
        return availabilities.stream()
                .map(Availability::getId)
                .collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "availabilities", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(AdministrativeDTO dto, @MappingTarget Administrative entity);
}