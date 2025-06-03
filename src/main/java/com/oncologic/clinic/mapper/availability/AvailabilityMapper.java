package com.oncologic.clinic.mapper.availability;

import com.oncologic.clinic.dto.availability.AvailabilityDTO;
import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.availability.Status;
import com.oncologic.clinic.entity.personal.Personal;
import org.mapstruct.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {StatusMapper.class})
public interface AvailabilityMapper {

    @Mapping(target = "personals", ignore = true) // It will be handled separately
    @Mapping(target = "status", source = "statusId", qualifiedByName = "mapIdToStatus")
    Availability toEntity(AvailabilityDTO dto);

    @Mapping(target = "personalIds", source = "personals", qualifiedByName = "mapPersonalsToIds")
    @Mapping(target = "status", source = "status")
    AvailabilityResponseDTO toDto(Availability entity);

    // ID to Status Mapping
    @Named("mapIdToStatus")
    default Status mapIdToStatus(Long statusId) {
        if (statusId == null) return null;
        Status status = new Status();
        status.setId(statusId);
        return status;
    }

    @Named("mapPersonalsToIds")
    default Set<Long> mapPersonalsToIds(Set<Personal> personals) {
        if (personals == null || personals.isEmpty()) {
            return new HashSet<>();
        }
        return personals.stream()
                .map(Personal::getId)
                .collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "personals", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntityFromDto(AvailabilityDTO dto, @MappingTarget Availability entity);
}