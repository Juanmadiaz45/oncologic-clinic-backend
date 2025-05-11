package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.mapper.personal.AdministrativeMapper;
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
    private final AdministrativeMapper administrativeMapper;


    AdministrativeServiceImpl(AdministrativeRepository administrativeRepository, UserService userService, AdministrativeMapper administrativeMapper) {
        this.administrativeRepository = administrativeRepository;
        this.userService = userService;
        this.administrativeMapper = administrativeMapper;
    }

    @Override
    public AdministrativeResponseDTO getAdministrativeById(Long id) {
        Administrative administrative = administrativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrativo con ID " + id + " no encontrado"));

        return administrativeMapper.toDto(administrative);
    }


    @Override
    public List<AdministrativeResponseDTO> getAllAdministrative() {
        return administrativeRepository.findAll().stream().map(administrativeMapper::toDto).toList();
    }

    @Override
    @Transactional
    public AdministrativeResponseDTO createAdministrative(AdministrativeDTO administrativeDTO) {
        if (administrativeDTO.getPosition() == null || administrativeDTO.getPosition().isEmpty()) {
            throw new IllegalArgumentException("El puesto administrativo no puede estar vacío");
        }

        UserDTO userRequestDTO = administrativeDTO.getPersonalData().getUserData();
        UserResponseDTO userResponse = userService.createUser(userRequestDTO);

        User user = userService.getUserEntityById(userResponse.getId());

        Administrative administrative = administrativeMapper.toEntity(administrativeDTO);
        administrative.setUser(user);
        administrative.setDateOfHiring(LocalDateTime.now());
        administrative.setStatus('A');

        Administrative savedAdministrative = administrativeRepository.save(administrative);

        return administrativeMapper.toDto(savedAdministrative);
    }



    @Override
    @Transactional
    public AdministrativeResponseDTO updateAdministrative(Long id, AdministrativeDTO updateDTO) {
        Administrative existingAdministrative = administrativeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Administrativo con ID " + id + " no encontrado"));

        administrativeMapper.updateEntityFromDto(updateDTO, existingAdministrative);

        Administrative updatedAdministrative = administrativeRepository.save(existingAdministrative);

        return administrativeMapper.toDto(updatedAdministrative);
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
