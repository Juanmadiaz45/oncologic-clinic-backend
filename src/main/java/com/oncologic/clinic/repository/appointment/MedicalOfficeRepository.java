package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.MedicalOffice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalOfficeRepository extends JpaRepository<MedicalOffice, Long> {
}
