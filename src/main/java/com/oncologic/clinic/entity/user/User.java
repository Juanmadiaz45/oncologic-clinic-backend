package com.oncologic.clinic.entity.user;

import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.personal.Personal;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"patient", "personal", "userRoles"})
@ToString(exclude = {"userRoles"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, length = 200)
    private String username;

    @Column(name = "password", nullable = false, length = 200)
    private String password;

    @OneToOne(mappedBy = "user")
    private Patient patient;

    @OneToOne(mappedBy = "user")
    private Personal personal;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();
}

