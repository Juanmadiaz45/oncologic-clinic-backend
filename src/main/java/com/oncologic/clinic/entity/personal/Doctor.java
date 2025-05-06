package com.oncologic.clinic.entity.personal;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Entity
@Table(name = "DOCTORS")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Doctor extends Personal {

    @Column(name = "medical_license_number", nullable = false, length = 200)
    private String medicalLicenseNumber;

    @OneToMany(mappedBy = "doctor")
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "DOCTOR_SPECIALITY",
            joinColumns = @JoinColumn(name = "doctor_id"),
            inverseJoinColumns = @JoinColumn(name = "speciality_id")
    )
    private Set<Speciality> specialities = new HashSet<>();
}


