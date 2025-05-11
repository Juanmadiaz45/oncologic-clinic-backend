package com.oncologic.clinic.mapper.availability;

import com.oncologic.clinic.dto.availability.AvailabilityDTO;
import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AvailabilityMapper {
    @Mapping(target = "personals", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    Availability toEntity(AvailabilityDTO dto);

    AvailabilityResponseDTO toDto(Availability entity);

    @Mapping(target = "personals", ignore = true)
    @Mapping(target = "statuses", ignore = true)
    void updateEntityFromDto(AvailabilityDTO dto, @MappingTarget Availability entity);
}
