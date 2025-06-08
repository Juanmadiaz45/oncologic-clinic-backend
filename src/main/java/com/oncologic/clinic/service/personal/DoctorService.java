package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.DoctorDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.entity.personal.Doctor;

import java.util.List;

public interface DoctorService {
    DoctorResponseDTO getDoctorById(Long id);

    List<DoctorResponseDTO> getAllDoctors();

    Doctor registerDoctor(RegisterDoctorDTO doctorDTO);

    DoctorResponseDTO createDoctor(DoctorDTO doctorDTO);

    DoctorResponseDTO updateDoctor(Long id, DoctorDTO doctorDTO);

    void deleteDoctor(Long id);

    List<DoctorResponseDTO> searchDoctorsByName(String name);

    List<DoctorResponseDTO> getDoctorsBySpeciality(Long specialityId);
}
