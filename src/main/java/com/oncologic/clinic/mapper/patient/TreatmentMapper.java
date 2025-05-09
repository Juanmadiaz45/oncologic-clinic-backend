package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.TreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TreatmentUpdateDTO;
import com.oncologic.clinic.entity.patient.Treatment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TreatmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "appointmentResult.id", source = "appointmentResultId")
    @Mapping(target = "typeOfTreatments", ignore = true)
    @Mapping(target = "prescribedMedicines", ignore = true)
    @Mapping(target = "medicalAppointments", ignore = true)
    Treatment toEntity(TreatmentRequestDTO dto);

    @Mapping(target = "appointmentResultId", source = "appointmentResult.id")
    @Mapping(target = "typeOfTreatmentIds", expression = "java(entity.getTypeOfTreatments().stream().map(t -> t.getId()).collect(Collectors.toList()))")
    @Mapping(target = "prescribedMedicineIds", expression = "java(entity.getPrescribedMedicines().stream().map(m -> m.getId()).collect(Collectors.toList()))")
    @Mapping(target = "medicalAppointmentIds", expression = "java(entity.getMedicalAppointments().stream().map(a -> a.getId()).collect(Collectors.toList()))")
    TreatmentResponseDTO toDto(Treatment entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(TreatmentUpdateDTO dto, @MappingTarget Treatment entity);
}
