package com.oncologic.clinic.repository.patient;

import com.oncologic.clinic.entity.patient.AppointmentResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentResultRepository extends JpaRepository<AppointmentResult, Long> {
}
