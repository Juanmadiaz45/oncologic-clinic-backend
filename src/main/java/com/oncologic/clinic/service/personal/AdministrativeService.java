package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.registration.RegisterAdministrativeDTO;
import com.oncologic.clinic.entity.personal.Administrative;

public interface AdministrativeService {
    Administrative registerAdministrative(RegisterAdministrativeDTO AdministrativeDTO);
}
