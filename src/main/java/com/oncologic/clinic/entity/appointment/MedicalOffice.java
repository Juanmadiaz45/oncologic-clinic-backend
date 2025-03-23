package com.oncologic.clinic.entity.appointment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEDICAL_OFFICES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_appointment_id")
    private MedicalAppointment medicalAppointment;

    @Column(name = "name", nullable = false, length = 200)
    private String name;
}
