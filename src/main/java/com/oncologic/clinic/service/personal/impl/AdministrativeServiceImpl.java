package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.PersonalDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.registration.RegisterAdministrativeDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.exception.runtime.personal.AdministrativeNotFoundException;
import com.oncologic.clinic.mapper.personal.AdministrativeMapper;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.repository.personal.AdministrativeRepository;
import com.oncologic.clinic.service.personal.AdministrativeService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class AdministrativeServiceImpl extends BasePersonalService<Administrative> implements AdministrativeService {

    private final AdministrativeRepository administrativeRepository;
    private final AdministrativeMapper administrativeMapper;

    public AdministrativeServiceImpl(AdministrativeRepository administrativeRepository,
                                     UserService userService,
                                     AdministrativeMapper administrativeMapper,
                                     AvailabilityRepository availabilityRepository) {
        super(userService, availabilityRepository);
        this.administrativeRepository = administrativeRepository;
        this.administrativeMapper = administrativeMapper;
    }

    @Override
    public AdministrativeResponseDTO getAdministrativeById(Long id) {
        Administrative administrative = administrativeRepository.findById(id)
                .orElseThrow(() -> new AdministrativeNotFoundException(id));
        return administrativeMapper.toDto(administrative);
    }

    @Override
    public List<AdministrativeResponseDTO> getAllAdministrative() {
        return administrativeRepository.findAll().stream()
                .map(administrativeMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public Administrative registerAdministrative(RegisterAdministrativeDTO administrativeDTO) {
        try {
            // Create user first
            User user = userService.createUser(administrativeDTO);

            // Create administrative with default values
            Administrative administrative = new Administrative();
            administrative.setUser(user);
            administrative.setIdNumber(administrativeDTO.getIdNumber());
            administrative.setName(administrativeDTO.getName());
            administrative.setLastName(administrativeDTO.getLastname());
            administrative.setEmail(administrativeDTO.getEmail());
            administrative.setPhoneNumber(administrativeDTO.getPhoneNumber());
            administrative.setPosition(administrativeDTO.getPosition());
            administrative.setDepartment(administrativeDTO.getDepartment());
            administrative.setDateOfHiring(LocalDateTime.now());
            administrative.setStatus('A'); // Active by default

            return administrativeRepository.save(administrative);

        } catch (Exception e) {
            throw new RuntimeException("Error registering administrative staff: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public AdministrativeResponseDTO createAdministrative(AdministrativeDTO administrativeDTO) {
        // Administrative-specific validations
        validateAdministrativeData(administrativeDTO);
        validateRequiredData(administrativeDTO.getPersonalData());

        // Create RegisterAdministrativeDTO to reuse existing logic
        RegisterAdministrativeDTO registerDto = mapToRegisterDTO(administrativeDTO);

        // Create administrative using existing method
        Administrative administrative = registerAdministrative(registerDto);

        // Assign availabilities if present
        if (administrativeDTO.getPersonalData().getAvailabilityIds() != null &&
                !administrativeDTO.getPersonalData().getAvailabilityIds().isEmpty()) {
            Set<Availability> availabilities = validateAndGetAvailabilities(
                    administrativeDTO.getPersonalData().getAvailabilityIds());
            administrative.setAvailabilities(availabilities);
            administrative = administrativeRepository.save(administrative);
        }

        return administrativeMapper.toDto(administrative);
    }

    @Override
    @Transactional
    public AdministrativeResponseDTO updateAdministrative(Long id, AdministrativeDTO updateDTO) {
        Administrative existingAdministrative = administrativeRepository.findById(id)
                .orElseThrow(() -> new AdministrativeNotFoundException(id));

        // Update basic administrative fields
        administrativeMapper.updateEntityFromDto(updateDTO, existingAdministrative);

        // Update user data using base method
        updateUserData(updateDTO.getPersonalData(), existingAdministrative.getUser());

        // Update personal data using base method
        updatePersonalData(updateDTO.getPersonalData(), existingAdministrative);

        Administrative updatedAdministrative = administrativeRepository.save(existingAdministrative);
        return administrativeMapper.toDto(updatedAdministrative);
    }

    @Override
    @Transactional
    public void deleteAdministrative(Long id) {
        Administrative administrative = administrativeRepository.findById(id)
                .orElseThrow(() -> new AdministrativeNotFoundException(id));

        administrativeRepository.delete(administrative);
    }

    /**
     * Administrative-specific validations
     */
    private void validateAdministrativeData(AdministrativeDTO administrativeDTO) {
        if (administrativeDTO.getPosition() == null || administrativeDTO.getPosition().isEmpty()) {
            throw new IllegalArgumentException("Position cannot be empty");
        }

        if (administrativeDTO.getDepartment() == null || administrativeDTO.getDepartment().isEmpty()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
    }

    /**
     * Maps AdministrativeDTO to RegisterAdministrativeDTO to reuse logic
     */
    private RegisterAdministrativeDTO mapToRegisterDTO(AdministrativeDTO administrativeDTO) {
        PersonalDTO personalData = administrativeDTO.getPersonalData();

        RegisterAdministrativeDTO registerDto = new RegisterAdministrativeDTO();

        // Personal data
        registerDto.setIdNumber(personalData.getIdNumber());
        registerDto.setName(personalData.getName());
        registerDto.setLastname(personalData.getLastName());
        registerDto.setEmail(personalData.getEmail());
        registerDto.setPhoneNumber(personalData.getPhoneNumber());

        // Administrative data
        registerDto.setPosition(administrativeDTO.getPosition());
        registerDto.setDepartment(administrativeDTO.getDepartment());

        // User data
        UserDTO userData = personalData.getUserData();
        registerDto.setUsername(userData.getUsername());
        registerDto.setPassword(userData.getPassword());
        registerDto.setRoleIds(userData.getRoleIds());

        return registerDto;
    }
}