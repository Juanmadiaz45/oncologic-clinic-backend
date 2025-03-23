package com.oncologic.clinic.entity.examination;

import com.oncologic.clinic.entity.patient.MedicalHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "EXAMINATION_RESULTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExaminationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "generation_date", nullable = false)
    private LocalDateTime generationDate;

    @Lob
    @Column(name = "results_report", nullable = false)
    private byte[] resultsReport;

    @ManyToOne
    @JoinColumn(name = "medical_history_id", nullable = false)
    private MedicalHistory medicalHistory;
}
