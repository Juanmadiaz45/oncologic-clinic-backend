package com.oncologic.clinic.dto.examination.update;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicalExaminationUpdateDTO {
    private LocalDateTime dateOfRealization;
    private Long laboratoryId;
    private Long typeOfExamId;
}