package com.oncologic.clinic.mapper.personal;

import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.dto.personal.update.PersonalUpdateDTO;
import com.oncologic.clinic.entity.personal.Personal;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonalMapper {
    Personal toEntity(PersonalRequestDTO dto);
    PersonalResponseDTO toDto(Personal entity);
    void updateEntityFromDto(PersonalUpdateDTO dto, @MappingTarget Personal entity);

    default Personal mapPersonalId(Long id) {
        if (id == null) return null;
        Personal personal = new Personal();
        personal.setId(id);
        return personal;
    }
}
