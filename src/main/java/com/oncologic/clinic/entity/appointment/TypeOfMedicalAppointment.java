package com.oncologic.clinic.entity.appointment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TYPE_OF_MEDICAL_APPOINTMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfMedicalAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @OneToMany(mappedBy = "typeOfMedicalAppointment", cascade = CascadeType.ALL)
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();
}


