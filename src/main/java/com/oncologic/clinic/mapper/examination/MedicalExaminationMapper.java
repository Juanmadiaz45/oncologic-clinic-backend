package com.oncologic.clinic.mapper.examination;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.entity.examination.MedicalExamination;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MedicalExaminationMapper {
    @Mapping(target = "laboratory", ignore = true)
    @Mapping(target = "typeOfExam", ignore = true)
    @Mapping(target = "medicalHistory", ignore = true)
    MedicalExamination toEntity(MedicalExaminationRequestDTO dto);

    @Mapping(source = "laboratory.id", target = "laboratoryId")
    @Mapping(source = "typeOfExam.id", target = "typeOfExamId")
    @Mapping(source = "medicalHistory.id", target = "medicalHistoryId")
    MedicalExaminationResponseDTO toDto(MedicalExamination entity);

    @BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "laboratory", ignore = true)
    @Mapping(target = "typeOfExam", ignore = true)
    @Mapping(target = "medicalHistory", ignore = true)
    void updateEntityFromDto(MedicalExaminationUpdateDTO dto, @MappingTarget MedicalExamination entity);
}
