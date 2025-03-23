package com.oncologic.clinic.entity.patient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PRESCRIBED_MEDICINES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescribedMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "medicine", nullable = false, length = 200)
    private String medicine;

    @Column(name = "prescription_date", nullable = false)
    private LocalDateTime prescriptionDate;

    @Column(name = "instructions", nullable = false, length = 200)
    private String instructions;

    @Column(name = "dose", nullable = false, length = 200)
    private String dose;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "frequency_of_administration", nullable = false, length = 200)
    private String frequencyOfAdministration;

    @ManyToOne
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;
}

