package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.personal.Doctor;

import java.util.List;

public interface PatientService {
    Patient getPatientById(Long id);
    List<Patient> getAllPatients();
    Patient registerPatient(RegisterPatientDTO patientDTO);
    Patient updatePatient(Long id, Patient patient);
    void deletePatient(Long id);
}

