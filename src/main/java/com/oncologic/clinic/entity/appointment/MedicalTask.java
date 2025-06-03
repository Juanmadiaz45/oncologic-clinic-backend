package com.oncologic.clinic.entity.appointment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MEDICAL_TASKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"medicalAppointments"})
@ToString(exclude = {"medicalAppointments"})
public class MedicalTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "estimated_time", nullable = false)
    private Long estimatedTime;

    @Column(name = "status", nullable = false, length = 200)
    private String status;

    @Column(name = "responsible", nullable = false, length = 200)
    private String responsible;

    @ManyToMany(mappedBy = "medicalTasks", fetch = FetchType.LAZY)
    @JsonBackReference("appointment-tasks")
    private Set<MedicalAppointment> medicalAppointments = new HashSet<>();
}

