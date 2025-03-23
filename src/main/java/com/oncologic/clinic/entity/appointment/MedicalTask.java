package com.oncologic.clinic.entity.appointment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "MEDICAL_TASKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToMany(mappedBy = "medicalTasks")
    private Set<MedicalAppointment> medicalAppointments = new HashSet<>();
}

