package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.request.AdministrativeRequestDTO;
import com.oncologic.clinic.dto.personal.request.DoctorRequestDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.response.DoctorResponseDTO;
import com.oncologic.clinic.dto.personal.update.AdministrativeUpdateDTO;
import com.oncologic.clinic.dto.personal.update.DoctorUpdateDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.entity.personal.Doctor;

import java.util.List;

public interface DoctorService {
    DoctorResponseDTO getDoctorById(Long id);
    List<DoctorResponseDTO> getAllDoctors();
    DoctorResponseDTO createDoctor(DoctorRequestDTO doctorDTO);
    DoctorResponseDTO updateDoctor(Long id, DoctorUpdateDTO doctorDTO);
    void deleteDoctor(Long id);
}
