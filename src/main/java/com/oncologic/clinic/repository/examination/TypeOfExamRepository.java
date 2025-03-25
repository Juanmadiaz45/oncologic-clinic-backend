package com.oncologic.clinic.repository.examination;

import com.oncologic.clinic.entity.examination.TypeOfExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfExamRepository extends JpaRepository<TypeOfExam, Long> {
}
