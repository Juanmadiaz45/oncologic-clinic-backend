package com.oncologic.clinic.entity.patient;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "OBSERVATIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 200)
    private String content;

    @Column(name = "recommendation", length = 250)
    private String recommendation;

    @ManyToOne
    @JoinColumn(name = "appointment_result_id", nullable = false)
    private AppointmentResult appointmentResult;
}

