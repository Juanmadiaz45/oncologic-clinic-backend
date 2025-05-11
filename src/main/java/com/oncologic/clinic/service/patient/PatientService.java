package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.PatientRequestDTO;
import com.oncologic.clinic.dto.patient.response.PatientResponseDTO;
import com.oncologic.clinic.dto.patient.update.PatientUpdateDTO;
import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.personal.Doctor;

import java.util.List;

public interface PatientService {
    PatientResponseDTO getPatientById(Long id);
    List<PatientResponseDTO> getAllPatients();
    Patient registerPatient(RegisterPatientDTO patientDTO);
    PatientResponseDTO createPatient(PatientRequestDTO patientDTO);
    PatientResponseDTO updatePatient(Long id, PatientUpdateDTO patientDTO);
    void deletePatient(Long id);
}

