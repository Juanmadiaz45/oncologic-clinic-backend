package com.oncologic.clinic.repository.patient;

import com.oncologic.clinic.entity.patient.PrescribedMedicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescribedMedicineRepository extends JpaRepository<PrescribedMedicine, Long> {
    List<PrescribedMedicine> findByTreatmentId(Long treatmentId);
}
