package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.registration.RegisterAdministrativeDTO;
import com.oncologic.clinic.entity.personal.Administrative;

import java.util.List;

public interface AdministrativeService {
    AdministrativeResponseDTO getAdministrativeById(Long id);

    Administrative registerAdministrative(RegisterAdministrativeDTO administrativeDTO);

    List<AdministrativeResponseDTO> getAllAdministrative();

    AdministrativeResponseDTO createAdministrative(AdministrativeDTO AdministrativeDTO);

    AdministrativeResponseDTO updateAdministrative(Long id, AdministrativeDTO updateDTO);

    void deleteAdministrative(Long id);
}
