package com.oncologic.clinic.dto.patient.response;

import com.oncologic.clinic.dto.user.request.UserRequestDTO;
import lombok.Data;

@Data
public class PatientResponseDTO {
    private UserRequestDTO userData;
    private Long medicalHistoryId;
    private String name;
    private String birthDate;
    private Character gender;
    private String address;
    private String phoneNumber;
    private String email;
    private String currentHealthStatus;
}
