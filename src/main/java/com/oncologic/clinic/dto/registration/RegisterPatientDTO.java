package com.oncologic.clinic.dto.registration;

import com.oncologic.clinic.dto.user.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientDTO {
    private UserDTO userData;
    private String idNumber;
    private Long medicalHistoryId;
    private String name;
    private String birthDate;
    private Character gender;
    private String address;
    private String phoneNumber;
    private String email;
    private String currentHealthStatus;
}
