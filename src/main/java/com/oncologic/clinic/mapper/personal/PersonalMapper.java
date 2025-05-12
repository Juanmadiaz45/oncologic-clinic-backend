package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.dto.personal.update.PersonalUpdateDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.personal.Personal;
import com.oncologic.clinic.mapper.user.UserMapper;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface PersonalMapper {
    Personal toEntity(PersonalRequestDTO dto);

    @Mapping(target = "availabilityIds", source = "availabilities", qualifiedByName = "mapAvailabilitiesToIds")
    PersonalResponseDTO toDto(Personal entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PersonalUpdateDTO dto, @MappingTarget Personal entity);

    @Named("mapAvailabilitiesToIds")
    default Set<Long> mapAvailabilitiesToIds(Set<Availability> availabilities) {
        if (availabilities == null) return new HashSet<>();
        return availabilities.stream()
                .map(Availability::getId)
                .collect(Collectors.toSet());
    }

}