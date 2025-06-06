package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.MedicalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicalOfficeRepository extends JpaRepository<MedicalOffice, Long> {

    // CAMBIO: Métodos para encontrar consultorios disponibles
    @Query("SELECT mo FROM MedicalOffice mo WHERE mo.id NOT IN " +
            "(SELECT DISTINCT ma.medicalOffice.id FROM MedicalAppointment ma " +
            "WHERE ma.medicalOffice.id IS NOT NULL " +
            "AND ma.appointmentDate BETWEEN :startTime AND :endTime)")
    List<MedicalOffice> findAvailableOffices(@Param("startTime") LocalDateTime startTime,
                                             @Param("endTime") LocalDateTime endTime);

    // Buscar consultorios con equipamiento específico
    @Query("SELECT mo FROM MedicalOffice mo WHERE mo.equipment LIKE %:equipment%")
    List<MedicalOffice> findByEquipmentContaining(@Param("equipment") String equipment);
}
