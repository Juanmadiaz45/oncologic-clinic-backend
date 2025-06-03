package com.oncologic.clinic.entity.appointment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "MEDICAL_OFFICES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"medicalAppointment"})
@ToString(exclude = {"medicalAppointment"})
public class MedicalOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_appointment_id")
    @JsonBackReference("appointment-offices")
    private MedicalAppointment medicalAppointment;

    @Column(name = "name", nullable = false, length = 200)
    private String name;
}
