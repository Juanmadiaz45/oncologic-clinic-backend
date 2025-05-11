package com.oncologic.clinic.dto.patient.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientUpdateDTO {
    private String name;
    private String birthDate;
    private Character gender;
    private String address;
    private String phoneNumber;
    private String email;
}
