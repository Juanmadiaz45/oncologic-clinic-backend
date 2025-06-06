package com.oncologic.clinic.mapper.appointment;

import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MedicalOfficeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicalAppointments", ignore = true)
    MedicalOffice toEntity(MedicalOfficeDTO dto);

    @Mapping(target = "medicalAppointmentIds", source = "medicalAppointments", qualifiedByName = "mapAppointmentIds")
    MedicalOfficeResponseDTO toDto(MedicalOffice entity);

    @Named("mapAppointmentIds")
    default List<Long> mapAppointmentIds(List<MedicalAppointment> appointments) {
        if (appointments == null || appointments.isEmpty()) {
            return new ArrayList<>();
        }
        return appointments.stream()
                .map(MedicalAppointment::getId)
                .collect(Collectors.toList());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicalAppointments", ignore = true)
        // CAMBIO
    void updateEntityFromDto(MedicalOfficeDTO dto, @MappingTarget MedicalOffice entity);
}
