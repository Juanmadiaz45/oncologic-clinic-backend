package com.oncologic.clinic.repository.patient;

import com.oncologic.clinic.entity.patient.TypeOfTreatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfTreatmentRepository extends JpaRepository<TypeOfTreatment, Long> {
}
