package com.oncologic.clinic.repository.personal;

import com.oncologic.clinic.entity.personal.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByNameContainingIgnoreCase(String name);

    @Query("SELECT d FROM Doctor d JOIN d.specialities s WHERE s.id = :specialityId")
    List<Doctor> findBySpecialityId(@Param("specialityId") Long specialityId);
}
