package com.oncologic.clinic.entity.appointment;

import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.entity.personal.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "MEDICAL_APPOINTMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "type_of_medical_appointment_id", nullable = false)
    private TypeOfMedicalAppointment typeOfMedicalAppointment;

    @Column(name = "appointment_date", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "id3", nullable = false)
    private Long id3;

    @ManyToOne
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    @ManyToOne
    @JoinColumn(name = "medical_history_id", nullable = false)
    private MedicalHistory medicalHistory;

    @OneToMany(mappedBy = "medicalAppointment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalOffice> medicalOffices;

    @ManyToMany
    @JoinTable(
            name = "APPOINTMENT_TASKS",
            joinColumns = @JoinColumn(name = "medical_appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_task_id")
    )
    private Set<MedicalTask> medicalTasks = new HashSet<>();
}
