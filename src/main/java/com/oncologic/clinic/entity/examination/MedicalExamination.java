package com.oncologic.clinic.entity.examination;

import com.oncologic.clinic.entity.patient.MedicalHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEDICAL_EXAMINATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalExamination {

    @Id
    private String id;

    @Column(name = "date_of_realization", nullable = false)
    private LocalDateTime dateOfRealization;

    @ManyToOne
    @JoinColumn(name = "laboratory_id", nullable = false)
    private Laboratory laboratory;

    @ManyToOne
    @JoinColumn(name = "type_of_exam_id", nullable = false)
    private TypeOfExam typeOfExam;

    @ManyToOne
    @JoinColumn(name = "medical_history_id", nullable = false)
    private MedicalHistory medicalHistory;
}

