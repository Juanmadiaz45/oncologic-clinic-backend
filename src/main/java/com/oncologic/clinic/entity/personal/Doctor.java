package com.oncologic.clinic.entity.personal;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "doctor")
    private List<Speciality> specialities = new ArrayList<>();
}


