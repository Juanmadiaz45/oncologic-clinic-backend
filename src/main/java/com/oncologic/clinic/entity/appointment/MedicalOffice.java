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
@Table(name = "MEDICAL_OFFICES")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"medicalAppointments"})
@ToString(exclude = {"medicalAppointments"})
public class MedicalOffice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "location", length = 300)
    private String location;

    @Column(name = "equipment", length = 500)
    private String equipment;

    @OneToMany(mappedBy = "medicalOffice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("office-appointments")
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();
}
