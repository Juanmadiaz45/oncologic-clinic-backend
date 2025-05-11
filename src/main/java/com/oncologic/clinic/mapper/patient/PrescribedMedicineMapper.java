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
    @Mapping(target = "medicine", source = "medicine")
    @Mapping(target = "prescriptionDate", source = "prescriptionDate")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "dose", source = "dose")
    @Mapping(target = "duration", source = "duration")
    @Mapping(target = "frequencyOfAdministration", source = "frequencyOfAdministration")
    PrescribedMedicine toEntity(PrescribedMedicineRequestDTO dto);

    @Mapping(target = "treatmentId", source = "treatment.id")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "medicine", source = "medicine")
    @Mapping(target = "prescriptionDate", source = "prescriptionDate")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "dose", source = "dose")
    @Mapping(target = "duration", source = "duration")
    @Mapping(target = "frequencyOfAdministration", source = "frequencyOfAdministration")
    PrescribedMedicineResponseDTO toDto(PrescribedMedicine entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "treatment", ignore = true)
    @Mapping(target = "medicine", source = "medicine")
    @Mapping(target = "prescriptionDate", source = "prescriptionDate")
    @Mapping(target = "instructions", source = "instructions")
    @Mapping(target = "dose", source = "dose")
    @Mapping(target = "duration", source = "duration")
    @Mapping(target = "frequencyOfAdministration", source = "frequencyOfAdministration")
    void updateEntityFromDto(PrescribedMedicineUpdateDTO dto, @MappingTarget PrescribedMedicine entity);
}
