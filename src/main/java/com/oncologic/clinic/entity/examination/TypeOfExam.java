package com.oncologic.clinic.entity.examination;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TYPE_OF_EXAMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", nullable = false, length = 250)
    private String description;

    @OneToMany(mappedBy = "typeOfExam")
    private List<MedicalExamination> medicalExaminations = new ArrayList<>();
}

