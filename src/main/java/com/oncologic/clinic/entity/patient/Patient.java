package com.oncologic.clinic.entity.patient;

import com.oncologic.clinic.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PATIENTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "birthdate", nullable = false)
    private LocalDateTime birthdate;

    @Column(name = "gender", nullable = false, length = 1)
    private Character gender;

    @Column(name = "address", nullable = false, length = 200)
    private String address;

    @Column(name = "phone_number", nullable = false, length = 200)
    private String phoneNumber;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private MedicalHistory medicalHistory;
}
