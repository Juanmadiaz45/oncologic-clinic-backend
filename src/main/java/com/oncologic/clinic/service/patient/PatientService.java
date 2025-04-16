package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.register.RegisterPatientDTO;
import com.oncologic.clinic.entity.patient.Patient;

public interface PatientService {
    Patient registerPatient(RegisterPatientDTO patientDTO);
}
