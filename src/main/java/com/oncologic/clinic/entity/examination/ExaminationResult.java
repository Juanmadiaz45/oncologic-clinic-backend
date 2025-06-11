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

    @Column(name = "results_report", columnDefinition = "TEXT")
    private String resultsReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_history_id", nullable = false)
    private MedicalHistory medicalHistory;
}