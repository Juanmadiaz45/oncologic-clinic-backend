package com.oncologic.clinic.dto.personal.response;


import com.oncologic.clinic.dto.SpecialityDTO;
import lombok.Data;
import java.util.Set;

@Data
public class DoctorResponseDTO extends PersonalResponseDTO {
    private String medicalLicenseNumber;
    private Set<SpecialityDTO> specialities;
}
