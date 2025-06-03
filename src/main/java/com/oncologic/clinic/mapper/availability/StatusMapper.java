package com.oncologic.clinic.mapper.availability;

import com.oncologic.clinic.dto.availability.StatusDTO;
import com.oncologic.clinic.dto.availability.response.StatusResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.availability.Status;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StatusMapper {

    Status toEntity(StatusDTO dto);

    @Mapping(target = "availabilityIds", source = "availabilities", qualifiedByName = "mapAvailabilitiesToIds")
    StatusResponseDTO toDto(Status entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "availabilities", ignore = true)
    void updateEntityFromDto(StatusDTO dto, @MappingTarget Status entity);

    @Named("mapAvailabilitiesToIds")
    default Set<Long> mapAvailabilitiesToIds(List<Availability> availabilities) {
        if (availabilities == null) {
            return Set.of();
        }
        return availabilities.stream()
                .map(Availability::getId)
                .collect(Collectors.toSet());
    }
}