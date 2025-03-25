package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.MedicalTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalTaskRepository extends JpaRepository<MedicalTask, Long> {
}
