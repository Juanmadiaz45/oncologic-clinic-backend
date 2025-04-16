package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.register.RegisterDoctorDTO;
import com.oncologic.clinic.entity.personal.Doctor;

public interface DoctorService {
    Doctor registerDoctor(RegisterDoctorDTO doctorDTO);
}
