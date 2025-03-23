package com.oncologic.clinic.entity.patient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "APPOINTMENT_RESULTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_date", nullable = false)
    private LocalDateTime evaluationDate;

    @ManyToOne
    @JoinColumn(name = "medical_history_id", nullable = false)
    private MedicalHistory medicalHistory;

    @OneToMany(mappedBy = "appointmentResult")
    private List<Observation> observations = new ArrayList<>();

    @OneToMany(mappedBy = "appointmentResult")
    private List<Treatment> treatments = new ArrayList<>();
}


