package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.MedicalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalOfficeRepository extends JpaRepository<MedicalOffice, Long> {
    @Query("SELECT mo.id FROM MedicalOffice mo WHERE mo.medicalAppointment.id = :appointmentId")
    List<Long> findOfficeIdsByAppointmentId(@Param("appointmentId") Long appointmentId);

    @Modifying
    @Query("UPDATE MedicalOffice mo SET mo.medicalAppointment.id = :appointmentId WHERE mo.id IN :officeIds")
    void updateAppointmentForOffices(@Param("appointmentId") Long appointmentId, @Param("officeIds") List<Long> officeIds);
}
