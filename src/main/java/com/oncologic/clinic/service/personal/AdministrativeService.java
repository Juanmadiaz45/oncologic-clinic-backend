package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;

import java.util.List;

public interface AdministrativeService {
    AdministrativeResponseDTO getAdministrativeById(Long id);
    List<AdministrativeResponseDTO> getAllAdministrative();
    AdministrativeResponseDTO createAdministrative(AdministrativeDTO AdministrativeDTO);
    AdministrativeResponseDTO updateAdministrative(Long id, AdministrativeDTO updateDTO);
    void deleteAdministrative(Long id);
}
