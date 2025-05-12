package com.oncologic.clinic.entity.patient;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TREATMENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Treatment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", nullable = false, length = 200)
    private String description;

    @Column(name = "date_start", nullable = false)
    private LocalDateTime dateStart;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "appointment_result_id", nullable = false)
    private AppointmentResult appointmentResult;

    @OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TypeOfTreatment> typeOfTreatments = new ArrayList<>();

    @OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PrescribedMedicine> prescribedMedicines = new ArrayList<>();

    @OneToMany(mappedBy = "treatment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicalAppointment> medicalAppointments = new ArrayList<>();
}

