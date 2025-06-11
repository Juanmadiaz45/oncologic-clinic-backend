package com.oncologic.clinic.repository.examination;

import com.oncologic.clinic.entity.examination.ExaminationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationResultRepository extends JpaRepository<ExaminationResult, Long> {
    List<ExaminationResult> findByMedicalHistoryId(Long medicalHistoryId);
}
