package com.oncologic.clinic.repository.patient;

import com.oncologic.clinic.entity.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findByIdNumberContaining(String idNumber);
}