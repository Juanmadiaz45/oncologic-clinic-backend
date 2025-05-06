package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.request.AdministrativeRequestDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.update.AdministrativeUpdateDTO;

import java.util.List;

public interface AdministrativeService {
    AdministrativeResponseDTO getAdministrativeById(Long id);
    List<AdministrativeResponseDTO> getAllAdministratives();
    AdministrativeResponseDTO createAdministrative(AdministrativeRequestDTO AdministrativeDTO);
    AdministrativeResponseDTO updateAdministrative(Long id, AdministrativeUpdateDTO administrativeDTO);
    void deleteAdministrative(Long id);
}
