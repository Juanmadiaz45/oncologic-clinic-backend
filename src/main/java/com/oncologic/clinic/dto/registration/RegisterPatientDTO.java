package com.oncologic.clinic.dto.registration;

import lombok.Data;

@Data
public class RegisterPatientDTO extends RegisterUserDTO{
    private Long medicalHistoryId;
    private String name;
    private String birthDate;
    private Character gender;
    private String address;
    private String phoneNumber;
    private String email;
    private String currentHealthStatus;
}
