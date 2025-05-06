package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.request.AdministrativeRequestDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.update.AdministrativeUpdateDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.mapper.PersonalMapper;
import com.oncologic.clinic.repository.personal.AdministrativeRepository;
import com.oncologic.clinic.service.personal.AdministrativeService;
import com.oncologic.clinic.service.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdministrativeServiceImpl implements AdministrativeService {
    private final AdministrativeRepository administrativeRepository;
    private final UserService userService;
    private final PersonalMapper personalMapper;


    AdministrativeServiceImpl(AdministrativeRepository administrativeRepository, UserService userService, PersonalMapper personalMapper) {
        this.administrativeRepository = administrativeRepository;
        this.userService = userService;
        this.personalMapper = personalMapper;
    }

    @Override
    public AdministrativeResponseDTO getAdministrativeById(Long id) {
        Administrative administrative = administrativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrativo con ID " + id + " no encontrado"));

        return personalMapper.toDto(administrative);
    }


    @Override
    public List<AdministrativeResponseDTO> getAllAdministratives() {
        return administrativeRepository.findAll().stream().map(personalMapper::toDto).toList();
    }

    @Override
    @Transactional
    public AdministrativeResponseDTO createAdministrative(AdministrativeRequestDTO administrativeDTO) {
        User user = userService.createUser(administrativeDTO);

        Administrative administrative = personalMapper.toEntity(administrativeDTO);
        administrative.setUser(user);
        administrative.setDateOfHiring(LocalDateTime.now());
        administrative.setStatus('A');

        Administrative savedAdministrative = administrativeRepository.save(administrative);

        return personalMapper.toDto(savedAdministrative);
    }



    @Override
    @Transactional
    public AdministrativeResponseDTO updateAdministrative(Long id, AdministrativeUpdateDTO updateDTO) {
        Administrative existingAdministrative = administrativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrativo con ID " + id + " no encontrado"));

        personalMapper.updateEntityFromDto(updateDTO, existingAdministrative);

        Administrative updatedAdministrative = administrativeRepository.save(existingAdministrative);

        return personalMapper.toDto(updatedAdministrative);
    }


    @Override
    @Transactional
    public void deleteAdministrative(Long id) {
        Administrative administrative = administrativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrativo no encontrado con el ID: " + id));
        if (administrative.getUser() != null) {
            userService.deleteUser(administrative.getUser().getId());
        }
        administrativeRepository.deleteById(id);
    }

}
