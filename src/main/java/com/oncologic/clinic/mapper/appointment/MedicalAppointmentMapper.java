package com.oncologic.clinic.mapper.appointment;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor.id", source = "doctorId")
    @Mapping(target = "typeOfMedicalAppointment.id", source = "typeOfMedicalAppointmentId")
    @Mapping(target = "treatment.id", source = "treatmentId")
    @Mapping(target = "medicalHistory.id", source = "medicalHistoryId")
    @Mapping(target = "medicalOffices", ignore = true)
    @Mapping(target = "medicalTasks", ignore = true)
    MedicalAppointment toEntity(MedicalAppointmentDTO dto);

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "typeOfMedicalAppointmentId", source = "typeOfMedicalAppointment.id")
    @Mapping(target = "treatmentId", source = "treatment.id")
    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    @Mapping(target = "medicalOfficeIds", source = "medicalOffices")
    @Mapping(target = "medicalTaskIds", source = "medicalTasks")
    MedicalAppointmentResponseDTO toDto(MedicalAppointment entity);

    default List<Long> mapMedicalOffices(List<MedicalOffice> medicalOffices) {
        if (medicalOffices == null) return null;
        return medicalOffices.stream().map(MedicalOffice::getId).collect(Collectors.toList());
    }

    default Set<Long> mapMedicalTasks(Set<MedicalTask> medicalTasks) {
        if (medicalTasks == null) return null;
        return medicalTasks.stream().map(MedicalTask::getId).collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "medicalOffices", ignore = true)
    @Mapping(target = "medicalTasks", ignore = true)
    void updateFromDto(MedicalAppointmentDTO dto, @MappingTarget MedicalAppointment entity);
}
