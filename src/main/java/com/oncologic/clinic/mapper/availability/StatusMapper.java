package com.oncologic.clinic.mapper.availability;

import com.oncologic.clinic.dto.availability.StatusDTO;
import com.oncologic.clinic.dto.availability.response.StatusResponseDTO;
import com.oncologic.clinic.entity.availability.Status;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    Status toEntity(StatusDTO dto);

    @Mapping(source = "availability.id", target = "availabilityId")
    StatusResponseDTO toDto(Status entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "availability", ignore = true)
    void updateEntityFromDto(StatusDTO dto, @MappingTarget Status entity);
}