package com.oncologic.clinic.repository.availability;

import com.oncologic.clinic.entity.availability.Availability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
}
