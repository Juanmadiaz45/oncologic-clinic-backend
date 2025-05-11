package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.DoctorDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;

import java.util.List;

public interface DoctorService {
    DoctorResponseDTO getDoctorById(Long id);
    List<DoctorResponseDTO> getAllDoctors();
    DoctorResponseDTO createDoctor(DoctorDTO doctorDTO);
    DoctorResponseDTO updateDoctor(Long id, DoctorDTO doctorDTO);
    void deleteDoctor(Long id);
}
