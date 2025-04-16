package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.register.RegisterAdministrativeDTO;
import com.oncologic.clinic.entity.personal.Administrative;

public interface AdministrativeService {
    Administrative registerAdministrative(RegisterAdministrativeDTO AdministrativeDTO);
}
