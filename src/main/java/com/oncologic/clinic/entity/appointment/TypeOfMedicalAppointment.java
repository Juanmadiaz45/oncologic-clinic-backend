package com.oncologic.clinic.entity.appointment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TYPE_OF_MEDICAL_APPOINTMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"medicalAppointments"})
@ToString(exclude = {"medicalAppointments"})
public class TypeOfMedicalAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @OneToMany(mappedBy = "typeOfMedicalAppointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("type-appointments")
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();
}


