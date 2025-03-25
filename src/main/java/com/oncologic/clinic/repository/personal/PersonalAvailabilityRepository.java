package com.oncologic.clinic.repository.personal;

import com.oncologic.clinic.entity.personal.PersonalAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalAvailabilityRepository extends JpaRepository<PersonalAvailability, PersonalAvailability.PersonalAvailabilityId> {
}
