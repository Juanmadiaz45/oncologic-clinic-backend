package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;

import java.util.List;

public interface AdministrativeService {
    AdministrativeResponseDTO getAdministrativeById(Long id);
    List<AdministrativeResponseDTO> getAllAdministratives();
    AdministrativeResponseDTO createAdministrative(AdministrativeDTO AdministrativeDTO);
    public AdministrativeResponseDTO updateAdministrative(Long id, AdministrativeDTO updateDTO);
    void deleteAdministrative(Long id);
}
