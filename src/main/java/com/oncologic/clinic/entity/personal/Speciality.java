package com.oncologic.clinic.entity.personal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "SPECIALITIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Speciality {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Speciality)) return false;
        Speciality that = (Speciality) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 250)
    private String description;

    @ManyToMany(mappedBy = "specialities")
    private Set<Doctor> doctors = new HashSet<>();
}

