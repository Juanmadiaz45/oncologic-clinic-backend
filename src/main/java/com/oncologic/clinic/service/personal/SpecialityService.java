package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.dto.personal.update.SpecialityUpdateDTO;
import com.oncologic.clinic.entity.personal.Doctor;

import java.util.List;

public interface SpecialityService {
    SpecialityResponseDTO getSpecialityById(Long id);
    List<SpecialityResponseDTO> getAllSpecialities();
    SpecialityResponseDTO createSpeciality(SpecialityRequestDTO specialityDTO);
    SpecialityResponseDTO updateSpeciality(Long id, SpecialityUpdateDTO specialityDTO);
    void deleteSpeciality(Long id);
}
