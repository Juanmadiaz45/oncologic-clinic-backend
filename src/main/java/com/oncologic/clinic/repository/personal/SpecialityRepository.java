package com.oncologic.clinic.repository.personal;

import com.oncologic.clinic.entity.personal.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Long> {
    Speciality findByName(String name);
}
