package com.oncologic.clinic.service.personal.impl;

import com.oncologic.clinic.dto.registration.RegisterAdministrativeDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.repository.personal.AdministrativeRepository;
import com.oncologic.clinic.service.personal.AdministrativeService;
import com.oncologic.clinic.service.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdministrativeServiceImpl implements AdministrativeService {
    private final AdministrativeRepository administrativeRepository;
    private final UserService userService;

    AdministrativeServiceImpl(AdministrativeRepository administrativeRepository, UserService userService) {
        this.administrativeRepository = administrativeRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Administrative registerAdministrative(RegisterAdministrativeDTO administrativeDTO) {
        User user = userService.createUser(administrativeDTO);

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
        administrative.setStatus('A');

        return administrativeRepository.save(administrative);
    }
}
