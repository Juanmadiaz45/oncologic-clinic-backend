package com.oncologic.clinic.dto.personal.update;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalUpdateDTO {
    private String idNumber;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Character status;
}
