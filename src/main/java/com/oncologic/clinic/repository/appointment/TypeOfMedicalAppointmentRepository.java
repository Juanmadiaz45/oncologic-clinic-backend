package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeOfMedicalAppointmentRepository extends JpaRepository<TypeOfMedicalAppointment, Long> {
    @Query("SELECT t FROM TypeOfMedicalAppointment t WHERE t.id IN :ids")
    List<TypeOfMedicalAppointment> findByIds(@Param("ids") List<Long> ids);
}
