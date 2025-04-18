package com.oncologic.clinic.dto.registration;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterPersonalDTO extends RegisterUserDTO{
    private String idNumber;
    private String name;
    private String lastname;
    private String email;
    private String phoneNumber;
    private LocalDateTime dateOfHiring;
    private Character status;
}
