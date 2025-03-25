package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointment, Long> {
}
