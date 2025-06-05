package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.personal.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalAppointmentRepository extends JpaRepository<MedicalAppointment, Long> {
    int countByDoctorAndAppointmentDateBetween(
            Doctor doctor,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("SELECT COUNT(DISTINCT ma.medicalHistory.patient.id) " +
            "FROM MedicalAppointment ma " +
            "WHERE ma.doctor = :doctor AND ma.appointmentDate > :date")
    int countDistinctPatientsByDoctorAndAppointmentDateAfter(
            @Param("doctor") Doctor doctor,
            @Param("date") LocalDateTime date
    );

    List<MedicalAppointment> findByDoctorAndAppointmentDateBetweenOrderByAppointmentDate(
            Doctor doctor,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    List<MedicalAppointment> findByDoctorAndAppointmentDateAfterOrderByAppointmentDate(
            Doctor doctor,
            LocalDateTime date
    );
}
