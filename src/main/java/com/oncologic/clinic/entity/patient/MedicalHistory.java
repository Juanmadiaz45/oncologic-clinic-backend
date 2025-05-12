package com.oncologic.clinic.entity.patient;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import com.oncologic.clinic.entity.examination.MedicalExamination;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MEDICAL_HISTORIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creationDate;

    @Column(name = "current_health_status", nullable = false, length = 200)
    private String currentHealthStatus;

    @OneToMany(mappedBy = "medicalHistory")
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();

    @OneToMany(mappedBy = "medicalHistory")
    private List<MedicalExamination> medicalExaminations = new ArrayList<>();

    @OneToMany(mappedBy = "medicalHistory")
    private List<AppointmentResult> appointmentResults = new ArrayList<>();

    @OneToMany(mappedBy = "medicalHistory")
    private List<ExaminationResult> examinationResults = new ArrayList<>();
}

