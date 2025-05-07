package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.dto.personal.update.SpecialityUpdateDTO;
import com.oncologic.clinic.entity.personal.Speciality;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SpecialityMapper {
    Speciality toEntity(SpecialityRequestDTO dto);
    SpecialityResponseDTO toDto(Speciality entity);
    void updateEntityFromDto(SpecialityUpdateDTO dto, @MappingTarget Speciality entity);
}
