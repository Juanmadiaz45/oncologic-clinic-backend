package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.MedicalTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MedicalTaskRepository extends JpaRepository<MedicalTask, Long> {
    @Query("SELECT mt.id FROM MedicalTask mt JOIN mt.medicalAppointments ma WHERE ma.id = :appointmentId")
    Set<Long> findTaskIdsByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Query("SELECT COUNT(mt) FROM MedicalTask mt WHERE mt.id IN :taskIds")
    long countByIdIn(@Param("taskIds") Set<Long> taskIds);
}
