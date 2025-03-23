package com.oncologic.clinic.entity.examination;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "LABORATORIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "location", nullable = false, length = 200)
    private String location;

    @Column(name = "telephone", nullable = false, length = 200)
    private String telephone;

    @OneToMany(mappedBy = "laboratory")
    private List<MedicalExamination> medicalExaminations = new ArrayList<>();
}
