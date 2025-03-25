package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOfMedicalAppointmentRepository extends JpaRepository<TypeOfMedicalAppointment, Long> {
}
