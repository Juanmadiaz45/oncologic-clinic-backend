package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.SpecialityDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.exception.runtime.personal.DoctorNotFoundException;
import com.oncologic.clinic.exception.runtime.personal.SpecialityNotFoundException;
import com.oncologic.clinic.mapper.personal.SpecialityMapper;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.SpecialityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SpecialityServiceImpl implements SpecialityService {
    private final SpecialityRepository specialityRepository;
    private final DoctorRepository doctorRepository;
    private final SpecialityMapper specialityMapper;

    public SpecialityServiceImpl(SpecialityRepository specialityRepository,
                                 DoctorRepository doctorRepository,
                                 SpecialityMapper specialityMapper) {
        this.specialityRepository = specialityRepository;
        this.doctorRepository = doctorRepository;
        this.specialityMapper = specialityMapper;
    }

    @Override
    public SpecialityResponseDTO getSpecialityById(Long id) {
        Speciality speciality = specialityRepository.findById(id)
                .orElseThrow(() -> new SpecialityNotFoundException(id));
        return specialityMapper.toDto(speciality);
    }

    @Override
    public List<SpecialityResponseDTO> getAllSpecialities() {
        return specialityRepository.findAll().stream()
                .map(specialityMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public SpecialityResponseDTO createSpeciality(SpecialityDTO specialityDTO) {
        Speciality speciality = specialityMapper.toEntity(specialityDTO);
        if (speciality.getName() == null || speciality.getName().isEmpty()) {
            throw new IllegalArgumentException("The specialty name cannot be empty");
        }
        if (specialityRepository.findByName(speciality.getName()) != null) {
            throw new IllegalArgumentException("The specialty name already exists");
        }
        // Associate doctors if IDs are provided
        if (specialityDTO.getDoctorIds() != null && !specialityDTO.getDoctorIds().isEmpty()) {
            Set<Doctor> doctors = validateAndGetDoctors(specialityDTO.getDoctorIds());
            assignDoctorsToSpeciality(speciality, doctors);
        }

        Speciality savedSpeciality = specialityRepository.save(speciality);
        return specialityMapper.toDto(savedSpeciality);
    }

    @Override
    @Transactional
    public SpecialityResponseDTO updateSpeciality(Long id, SpecialityDTO specialityDTO) {
        Speciality existingSpeciality = specialityRepository.findById(id)
                .orElseThrow(() -> new SpecialityNotFoundException(id));

        specialityMapper.updateEntityFromDto(specialityDTO, existingSpeciality);

        // Update doctors if IDs are provided
        if (specialityDTO.getDoctorIds() != null) {
            // Remove specialty from current doctors
            removeDoctorsFromSpeciality(existingSpeciality);

            // Assign new doctors
            if (!specialityDTO.getDoctorIds().isEmpty()) {
                Set<Doctor> doctors = validateAndGetDoctors(specialityDTO.getDoctorIds());
                assignDoctorsToSpeciality(existingSpeciality, doctors);
            }
        }

        Speciality updatedSpeciality = specialityRepository.save(existingSpeciality);
        return specialityMapper.toDto(updatedSpeciality);
    }

    @Override
    @Transactional
    public void deleteSpeciality(Long id) {
        Speciality speciality = specialityRepository.findById(id)
                .orElseThrow(() -> new SpecialityNotFoundException(id));

        // Remove the specialty from all doctors before deleting it
        removeDoctorsFromSpeciality(speciality);

        specialityRepository.delete(speciality);
    }

    /**
     * Validates that all doctor IDs exist and returns them
     */
    private Set<Doctor> validateAndGetDoctors(Set<Long> doctorIds) {
        Set<Doctor> doctors = new HashSet<>(doctorRepository.findAllById(doctorIds));

        if (doctors.size() != doctorIds.size()) {
            // Find which IDs do not exist
            Set<Long> foundIds = doctors.stream()
                    .map(Doctor::getId)
                    .collect(HashSet::new, HashSet::add, HashSet::addAll);

            Set<Long> missingIds = new HashSet<>(doctorIds);
            missingIds.removeAll(foundIds);

            throw new DoctorNotFoundException("Doctors not found with IDs: " + missingIds);
        }

        return doctors;
    }

    /**
     * Assign doctors to a specialty while maintaining a two-way relationship
     */
    private void assignDoctorsToSpeciality(Speciality speciality, Set<Doctor> doctors) {
        for (Doctor doctor : doctors) {
            // Assign specialty to the doctor
            if (doctor.getSpecialities() == null) {
                doctor.setSpecialities(new HashSet<>());
            }
            doctor.getSpecialities().add(speciality);

            // Assign a doctor to specialty
            if (speciality.getDoctors() == null) {
                speciality.setDoctors(new HashSet<>());
            }
            speciality.getDoctors().add(doctor);
        }
    }

    /**
     * Removes all doctors of a specialty
     */
    private void removeDoctorsFromSpeciality(Speciality speciality) {
        if (speciality.getDoctors() != null) {
            for (Doctor doctor : new HashSet<>(speciality.getDoctors())) {
                doctor.getSpecialities().remove(speciality);
            }
            speciality.getDoctors().clear();
        }
    }
}