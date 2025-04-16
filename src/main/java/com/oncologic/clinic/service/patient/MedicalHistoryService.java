package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;

public interface MedicalHistoryService {
    MedicalHistory registerMedicalHistory(Patient patient, String currentHealthStatus);
}
