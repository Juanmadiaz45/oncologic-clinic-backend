package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.PrescribedMedicineRequestDTO;
import com.oncologic.clinic.dto.patient.response.PrescribedMedicineResponseDTO;
import com.oncologic.clinic.dto.patient.update.PrescribedMedicineUpdateDTO;
import com.oncologic.clinic.entity.patient.PrescribedMedicine;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PrescribedMedicineMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "treatment.id", source = "treatmentId")
    PrescribedMedicine toEntity(PrescribedMedicineRequestDTO dto);

    @Mapping(target = "treatmentId", source = "treatment.id")
    PrescribedMedicineResponseDTO toDto(PrescribedMedicine entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(PrescribedMedicineUpdateDTO dto, @MappingTarget PrescribedMedicine entity);
}
