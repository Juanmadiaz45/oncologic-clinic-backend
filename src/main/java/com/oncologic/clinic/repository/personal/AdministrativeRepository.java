package com.oncologic.clinic.repository.personal;

import com.oncologic.clinic.entity.personal.Administrative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativeRepository extends JpaRepository<Administrative, Long> {
}
