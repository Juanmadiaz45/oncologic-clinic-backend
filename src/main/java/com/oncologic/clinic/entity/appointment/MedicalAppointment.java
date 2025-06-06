package com.oncologic.clinic.entity.appointment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.entity.personal.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MEDICAL_APPOINTMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"medicalTasks"})
@ToString(exclude = {"medicalTasks"})
public class MedicalAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_of_medical_appointment_id", nullable = false)
    private TypeOfMedicalAppointment typeOfMedicalAppointment;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_history_id", nullable = false)
    private MedicalHistory medicalHistory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_office_id")
    private MedicalOffice medicalOffice;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "APPOINTMENT_TASKS",
            joinColumns = @JoinColumn(name = "medical_appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_task_id")
    )
    @JsonManagedReference("appointment-tasks")
    private Set<MedicalTask> medicalTasks = new HashSet<>();
}
