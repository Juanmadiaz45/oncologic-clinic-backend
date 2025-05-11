package com.oncologic.clinic.dto.examination.update;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalExaminationUpdateDTO {
    private LocalDateTime dateOfRealization;
    private Long laboratoryId;
    private Long typeOfExamId;
}