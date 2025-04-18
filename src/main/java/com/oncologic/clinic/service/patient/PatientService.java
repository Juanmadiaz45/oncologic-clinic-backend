package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.entity.patient.Patient;

public interface PatientService {
    Patient registerPatient(RegisterPatientDTO patientDTO);
}
