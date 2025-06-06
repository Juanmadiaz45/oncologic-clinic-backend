package com.oncologic.clinic.mapper.appointment;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.entity.personal.Doctor;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "mapDoctor")
    @Mapping(target = "typeOfMedicalAppointment", source = "typeOfMedicalAppointmentId", qualifiedByName = "mapTypeOfAppointment")
    @Mapping(target = "treatment", source = "treatmentId", qualifiedByName = "mapTreatment")
    @Mapping(target = "medicalHistory", source = "medicalHistoryId", qualifiedByName = "mapMedicalHistory")
    @Mapping(target = "medicalOffice", source = "medicalOfficeId", qualifiedByName = "mapMedicalOffice") // CAMBIO
    @Mapping(target = "medicalTasks", ignore = true)
    MedicalAppointment toEntity(MedicalAppointmentDTO dto);

    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "typeOfMedicalAppointmentId", source = "typeOfMedicalAppointment.id")
    @Mapping(target = "treatmentId", source = "treatment.id")
    @Mapping(target = "medicalHistoryId", source = "medicalHistory.id")
    @Mapping(target = "medicalOfficeId", source = "medicalOffice.id") // CAMBIO
    @Mapping(target = "medicalTaskIds", source = "medicalTasks", qualifiedByName = "mapMedicalTaskIds")
    MedicalAppointmentResponseDTO toDto(MedicalAppointment entity);

    @Named("mapMedicalOffice")
    default MedicalOffice mapMedicalOffice(Long officeId) {
        if (officeId == null) return null;
        MedicalOffice office = new MedicalOffice();
        office.setId(officeId);
        return office;
    }

    @Named("mapMedicalTaskIds")
    default Set<Long> mapMedicalTaskIds(Set<MedicalTask> medicalTasks) {
        if (medicalTasks == null || medicalTasks.isEmpty()) {
            return null;
        }
        return medicalTasks.stream()
                .map(MedicalTask::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapDoctor")
    default Doctor mapDoctor(Long doctorId) {
        if (doctorId == null) return null;
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        return doctor;
    }

    @Named("mapTypeOfAppointment")
    default TypeOfMedicalAppointment mapTypeOfAppointment(Long typeId) {
        if (typeId == null) return null;
        TypeOfMedicalAppointment type = new TypeOfMedicalAppointment();
        type.setId(typeId);
        return type;
    }

    @Named("mapTreatment")
    default Treatment mapTreatment(Long treatmentId) {
        if (treatmentId == null) return null;
        Treatment treatment = new Treatment();
        treatment.setId(treatmentId);
        return treatment;
    }

    @Named("mapMedicalHistory")
    default MedicalHistory mapMedicalHistory(Long medicalHistoryId) {
        if (medicalHistoryId == null) return null;
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(medicalHistoryId);
        return medicalHistory;
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "typeOfMedicalAppointment", ignore = true)
    @Mapping(target = "treatment", ignore = true)
    @Mapping(target = "medicalHistory", ignore = true)
    @Mapping(target = "medicalOffice", ignore = true)
    @Mapping(target = "medicalTasks", ignore = true)
    void updateFromDto(MedicalAppointmentDTO dto, @MappingTarget MedicalAppointment entity);
}
