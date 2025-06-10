package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.MedicalHistoryRequestDTO;
import com.oncologic.clinic.dto.patient.response.MedicalHistoryResponseDTO;
import com.oncologic.clinic.dto.patient.update.MedicalHistoryUpdateDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import com.oncologic.clinic.entity.examination.MedicalExamination;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patient.id", source = "patientId")
    @Mapping(target = "medicalAppointments", ignore = true)
    @Mapping(target = "medicalExaminations", ignore = true)
    @Mapping(target = "appointmentResults", ignore = true)
    @Mapping(target = "examinationResults", ignore = true)
    MedicalHistory toEntity(MedicalHistoryRequestDTO dto);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "medicalAppointmentIds", source = "medicalAppointments", qualifiedByName = "mapAppointmentIds")
    @Mapping(target = "medicalExaminationIds", source = "medicalExaminations", qualifiedByName = "mapExaminationIds")
    @Mapping(target = "appointmentResultIds", source = "appointmentResults", qualifiedByName = "mapAppointmentResultIds")
    @Mapping(target = "examinationResultIds", source = "examinationResults", qualifiedByName = "mapExaminationResultIds")
    MedicalHistoryResponseDTO toDto(MedicalHistory entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(MedicalHistoryUpdateDTO dto, @MappingTarget MedicalHistory entity);

    @Named("mapAppointmentIds")
    default List<Long> mapAppointmentIds(List<com.oncologic.clinic.entity.appointment.MedicalAppointment> appointments) {
        if (appointments == null) return null;
        return appointments.stream().map(MedicalAppointment::getId).collect(Collectors.toList());
    }

    @Named("mapExaminationIds")
    default List<String> mapExaminationIds(List<com.oncologic.clinic.entity.examination.MedicalExamination> examinations) {
        if (examinations == null) return null;
        return examinations.stream().map(MedicalExamination::getId).collect(Collectors.toList());
    }

    @Named("mapAppointmentResultIds")
    default List<Long> mapAppointmentResultIds(List<com.oncologic.clinic.entity.patient.AppointmentResult> results) {
        if (results == null) return null;
        return results.stream().map(AppointmentResult::getId).collect(Collectors.toList());
    }

    @Named("mapExaminationResultIds")
    default List<Long> mapExaminationResultIds(List<ExaminationResult> results) {
        if (results == null || results.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return results.stream()
                    .filter(result -> result != null && result.getId() != null)
                    .map(ExaminationResult::getId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error mapping examination result IDs: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}