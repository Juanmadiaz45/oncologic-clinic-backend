package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.SpecialityDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.SpecialityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialityServiceImpl implements SpecialityService {
    private final SpecialityRepository specialityRepository;

    public SpecialityServiceImpl(SpecialityRepository specialityRepository) {
        this.specialityRepository = specialityRepository;
    }

    @Override
    public List<Speciality> getAllSpecialities() {
        return specialityRepository.findAll();
    }

    @Override
    public Speciality registerSpeciality(SpecialityDTO specialityDTO, Doctor doctor) {
        Speciality speciality = new Speciality();
        speciality.setName(specialityDTO.getName());
        speciality.setDescription(specialityDTO.getDescription());
        speciality.setDoctor(doctor);
        return specialityRepository.save(speciality);
    }

    @Override
    public Optional<Speciality> getSpecialityById(Long id) {
        return specialityRepository.findById(id);
    }
}
