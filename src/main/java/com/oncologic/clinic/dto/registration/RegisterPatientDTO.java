package com.oncologic.clinic.dto.registration;

import com.oncologic.clinic.dto.user.UserDTO;
import lombok.Data;

@Data
public class RegisterPatientDTO{
    private UserDTO userData;
    private Long medicalHistoryId;
    private String name;
    private String birthDate;
    private Character gender;
    private String address;
    private String phoneNumber;
    private String email;
    private String currentHealthStatus;
}
