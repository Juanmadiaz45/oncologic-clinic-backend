package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.DoctorDTO;
import com.oncologic.clinic.dto.personal.PersonalDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.exception.runtime.personal.DoctorNotFoundException;
import com.oncologic.clinic.exception.runtime.personal.SpecialityNotFoundException;
import com.oncologic.clinic.mapper.personal.DoctorMapper;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.DoctorService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoctorServiceImpl extends BasePersonalService<Doctor> implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialityRepository specialityRepository;
    private final DoctorMapper doctorMapper;

    public DoctorServiceImpl(DoctorRepository doctorRepository, UserService userService, SpecialityRepository specialityRepository, AvailabilityRepository availabilityRepository, DoctorMapper doctorMapper) {
        super(userService, availabilityRepository);
        this.doctorRepository = doctorRepository;
        this.specialityRepository = specialityRepository;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DoctorResponseDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        return doctorMapper.toDto(doctor);
    }

    @Override
    public List<DoctorResponseDTO> getAllDoctors() {
        return doctorRepository.findAll().stream().map(doctorMapper::toDto).toList();
    }

    @Override
    @Transactional
    public Doctor registerDoctor(RegisterDoctorDTO doctorDTO) {
        // Create user first
        User user = userService.createUser(doctorDTO);

        // Create doctor with default values
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setIdNumber(doctorDTO.getIdNumber());
        doctor.setName(doctorDTO.getName());
        doctor.setLastName(doctorDTO.getLastname());
        doctor.setEmail(doctorDTO.getEmail());
        doctor.setPhoneNumber(doctorDTO.getPhoneNumber());
        doctor.setMedicalLicenseNumber(doctorDTO.getMedicalLicenseNumber());
        doctor.setDateOfHiring(LocalDateTime.now());
        doctor.setStatus('A'); // Active by default

        Doctor savedDoctor = doctorRepository.save(doctor);

        // Assign specialties if present
        if (doctorDTO.getSpecialityIds() != null && !doctorDTO.getSpecialityIds().isEmpty()) {
            Set<Speciality> specialities = validateAndGetSpecialities(doctorDTO.getSpecialityIds());
            assignSpecialitiesToDoctor(savedDoctor, specialities);
        }

        return savedDoctor;

    }

    @Override
    @Transactional
    public DoctorResponseDTO createDoctor(DoctorDTO doctorDTO) {
        // Doctor-specific validations
        validateDoctorData(doctorDTO);
        validateRequiredData(doctorDTO.getPersonalData());

        // Create RegisterDoctorDTO to reuse existing logic
        RegisterDoctorDTO registerDto = mapToRegisterDTO(doctorDTO);

        // Create doctor using existing method
        Doctor doctor = registerDoctor(registerDto);

        // Assign availabilities if present
        if (getAvailabilityIds(doctorDTO) != null && !doctorDTO.getPersonalData().getAvailabilityIds().isEmpty()) {
            Set<Availability> availabilities = validateAndGetAvailabilities(doctorDTO.getPersonalData().getAvailabilityIds());
            doctor.setAvailabilities(availabilities);
            doctor = doctorRepository.save(doctor);
        }

        return doctorMapper.toDto(doctor);
    }

    private static Set<Long> getAvailabilityIds(DoctorDTO doctorDTO) {
        return doctorDTO.getPersonalData().getAvailabilityIds();
    }

    @Override
    @Transactional
    public DoctorResponseDTO updateDoctor(Long id, DoctorDTO updateDTO) {
        Doctor existingDoctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));

        // Update basic doctor fields
        doctorMapper.updateEntityFromDto(updateDTO, existingDoctor);

        // Update user data using base method
        updateUserData(updateDTO.getPersonalData(), existingDoctor.getUser());

        // Update personal data using base method
        updatePersonalData(updateDTO.getPersonalData(), existingDoctor);

        // Update specialties if present
        if (updateDTO.getSpecialityIds() != null) {
            // Remove current specialties
            removeSpecialitiesFromDoctor(existingDoctor);

            // Assign new specialties
            if (!updateDTO.getSpecialityIds().isEmpty()) {
                Set<Speciality> specialities = validateAndGetSpecialities(updateDTO.getSpecialityIds());
                assignSpecialitiesToDoctor(existingDoctor, specialities);
            }
        }

        Doctor updatedDoctor = doctorRepository.save(existingDoctor);
        return doctorMapper.toDto(updatedDoctor);
    }

    @Override
    @Transactional
    public void deleteDoctor(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id));
        doctorRepository.delete(doctor);
    }

    /**
     * Doctor-specific validations
     */
    private void validateDoctorData(DoctorDTO doctorDTO) {
        if (doctorDTO.getMedicalLicenseNumber() == null || doctorDTO.getMedicalLicenseNumber().isEmpty()) {
            throw new IllegalArgumentException("Medical license number cannot be empty");
        }
    }

    /**
     * Maps DoctorDTO to RegisterDoctorDTO to reuse logic
     */
    private RegisterDoctorDTO mapToRegisterDTO(DoctorDTO doctorDTO) {
        PersonalDTO personalData = doctorDTO.getPersonalData();

        RegisterDoctorDTO registerDto = new RegisterDoctorDTO();

        // Personal data
        registerDto.setIdNumber(personalData.getIdNumber());
        registerDto.setName(personalData.getName());
        registerDto.setLastname(personalData.getLastName());
        registerDto.setEmail(personalData.getEmail());
        registerDto.setPhoneNumber(personalData.getPhoneNumber());

        // Doctor data
        registerDto.setMedicalLicenseNumber(doctorDTO.getMedicalLicenseNumber());
        registerDto.setSpecialityIds(doctorDTO.getSpecialityIds());

        // User data
        UserDTO userData = personalData.getUserData();
        registerDto.setUsername(userData.getUsername());
        registerDto.setPassword(userData.getPassword());
        registerDto.setRoleIds(userData.getRoleIds());

        return registerDto;
    }

    /**
     * Validates that all specialty IDs exist and returns them
     */
    private Set<Speciality> validateAndGetSpecialities(Set<Long> specialityIds) {
        Set<Speciality> specialities = new HashSet<>(specialityRepository.findAllById(specialityIds));

        if (specialities.size() != specialityIds.size()) {
            Set<Long> foundIds = specialities.stream().map(Speciality::getId).collect(HashSet::new, HashSet::add, HashSet::addAll);

            Set<Long> missingIds = new HashSet<>(specialityIds);
            missingIds.removeAll(foundIds);

            throw new SpecialityNotFoundException("Specialties not found with IDs: " + missingIds);
        }

        return specialities;
    }

    /**
     * Assigns specialties to a doctor maintaining bidirectional relationship
     */
    private void assignSpecialitiesToDoctor(Doctor doctor, Set<Speciality> specialities) {
        for (Speciality speciality : specialities) {
            // Assign doctor to specialty
            if (speciality.getDoctors() == null) {
                speciality.setDoctors(new HashSet<>());
            }
            speciality.getDoctors().add(doctor);

            // Assign specialty to doctor
            if (doctor.getSpecialities() == null) {
                doctor.setSpecialities(new HashSet<>());
            }
            doctor.getSpecialities().add(speciality);
        }
    }

    /**
     * Removes all specialties from a doctor
     */
    private void removeSpecialitiesFromDoctor(Doctor doctor) {
        if (doctor.getSpecialities() != null) {
            for (Speciality speciality : new HashSet<>(doctor.getSpecialities())) {
                speciality.getDoctors().remove(doctor);
            }
            doctor.getSpecialities().clear();
        }
    }
}