package com.oncologic.clinic.repository.examination;

import com.oncologic.clinic.entity.examination.MedicalExamination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, String> {
}
