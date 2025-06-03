package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.SpecialityDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;

import java.util.List;

public interface SpecialityService {
    SpecialityResponseDTO getSpecialityById(Long id);
    List<SpecialityResponseDTO> getAllSpecialities();
    SpecialityResponseDTO createSpeciality(SpecialityDTO specialityDTO);
    SpecialityResponseDTO updateSpeciality(Long id, SpecialityDTO specialityDTO);
    void deleteSpeciality(Long id);
}
