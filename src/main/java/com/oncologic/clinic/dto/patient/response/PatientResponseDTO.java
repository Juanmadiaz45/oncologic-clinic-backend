package com.oncologic.clinic.dto.patient.response;

import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponseDTO {
    private Long id;
    private String idNumber;
    private UserResponseDTO userData;
    private String name;
    private String birthDate;
    private Character gender;
    private String address;
    private String phoneNumber;
    private String email;
    private MedicalHistoryResponseDTO medicalHistory;
}
