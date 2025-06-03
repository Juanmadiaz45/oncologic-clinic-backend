package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.PersonalDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.personal.Personal;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.exception.runtime.availability.AvailabilityNotFoundException;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.service.user.UserService;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base service containing common functionality for all Personal types
 */
public abstract class BasePersonalService<T extends Personal> {

    protected final UserService userService;
    protected final AvailabilityRepository availabilityRepository;

    protected BasePersonalService(UserService userService, AvailabilityRepository availabilityRepository) {
        this.userService = userService;
        this.availabilityRepository = availabilityRepository;
    }

    /**
     * Validates that all availability IDs exist and returns them
     */
    protected Set<Availability> validateAndGetAvailabilities(Set<Long> availabilityIds) {
        if (availabilityIds == null || availabilityIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<Availability> availabilities = new HashSet<>(availabilityRepository.findAllById(availabilityIds));

        if (availabilities.size() != availabilityIds.size()) {
            Set<Long> foundIds = availabilities.stream()
                    .map(Availability::getId)
                    .collect(HashSet::new, HashSet::add, HashSet::addAll);

            Set<Long> missingIds = new HashSet<>(availabilityIds);
            missingIds.removeAll(foundIds);

            throw new AvailabilityNotFoundException("Availabilities not found with IDs: " + missingIds);
        }

        return availabilities;
    }

    /**
     * Updates user data if present
     */
    protected void updateUserData(PersonalDTO personalData, User existingUser) {
        if (personalData != null && personalData.getUserData() != null) {
            UserDTO userData = personalData.getUserData();

            if (existingUser != null) {
                // Update username only if present (password is handled separately for security)
                if (userData.getUsername() != null) {
                    existingUser.setUsername(userData.getUsername());
                }

                // Update roles if present
                if (userData.getRoleIds() != null && !userData.getRoleIds().isEmpty()) {
                    userService.updateUserRoles(existingUser.getId(), userData.getRoleIds());
                }
            }
        }
    }

    /**
     * Updates basic personal data
     */
    protected void updatePersonalData(PersonalDTO personalData, T entity) {
        if (personalData != null) {
            if (personalData.getIdNumber() != null) {
                entity.setIdNumber(personalData.getIdNumber());
            }
            if (personalData.getName() != null) {
                entity.setName(personalData.getName());
            }
            if (personalData.getLastName() != null) {
                entity.setLastName(personalData.getLastName());
            }
            if (personalData.getEmail() != null) {
                entity.setEmail(personalData.getEmail());
            }
            if (personalData.getPhoneNumber() != null) {
                entity.setPhoneNumber(personalData.getPhoneNumber());
            }
            if (personalData.getStatus() != null) {
                entity.setStatus(personalData.getStatus());
            }

            // Update availabilities if present
            if (personalData.getAvailabilityIds() != null) {
                Set<Availability> availabilities = validateAndGetAvailabilities(personalData.getAvailabilityIds());
                entity.setAvailabilities(availabilities);
            }
        }
    }

    /**
     * Validates that required data is present
     */
    protected void validateRequiredData(PersonalDTO personalData) {
        if (personalData == null) {
            throw new IllegalArgumentException("Personal data is required");
        }

        if (personalData.getUserData() == null) {
            throw new IllegalArgumentException("User data is required");
        }
    }

}