package com.oncologic.clinic.mapper.patient;

import com.oncologic.clinic.dto.patient.request.PatientRequestDTO;
import com.oncologic.clinic.dto.patient.response.PatientResponseDTO;
import com.oncologic.clinic.dto.patient.update.PatientUpdateDTO;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.mapper.user.UserMapper;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MedicalHistoryMapper.class})
public interface PatientMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // It is handled manually in the service
    @Mapping(target = "birthdate", source = "birthDate", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "medicalHistory", ignore = true)
        // It is handled manually in the service
    Patient toEntity(PatientRequestDTO dto);

    @Mapping(target = "userData", source = "user") // Use UserMapper automatically
    @Mapping(target = "birthDate", source = "birthdate", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "medicalHistory", source = "medicalHistory")
        // Use MedicalHistoryMapper automatically
    PatientResponseDTO toDto(Patient entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "birthdate", source = "birthDate", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "medicalHistory", ignore = true)
    void updateEntityFromDto(PatientUpdateDTO dto, @MappingTarget Patient entity);

    // Convert String to LocalDateTime
    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return java.time.LocalDate.parse(dateString, formatter).atStartOfDay();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd, got: " + dateString);
        }
    }

    // Convert LocalDateTime to String
    @Named("localDateTimeToString")
    default String localDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.toLocalDate().format(formatter);
    }
}