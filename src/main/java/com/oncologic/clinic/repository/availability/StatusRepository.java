package com.oncologic.clinic.repository.availability;

import com.oncologic.clinic.entity.availability.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
}
