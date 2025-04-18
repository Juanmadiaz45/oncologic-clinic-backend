package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.SpecialityDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;

import java.util.List;
import java.util.Optional;

public interface SpecialityService {
    List<Speciality> getAllSpecialities();
    Speciality registerSpeciality(SpecialityDTO specialityDTO, Doctor doctor);
    Optional<Speciality> getSpecialityById(Long id);
}
