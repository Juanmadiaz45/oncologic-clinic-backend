package com.oncologic.clinic.repository.patient;

import com.oncologic.clinic.entity.patient.MedicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

@Repository
public interface MedicalHistoryRepository extends JpaRepository<MedicalHistory, Long> {

    boolean existsByPatientId(Long patientId);

}
