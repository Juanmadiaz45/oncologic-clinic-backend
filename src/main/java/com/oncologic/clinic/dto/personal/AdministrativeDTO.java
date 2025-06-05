package com.oncologic.clinic.dto.personal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeDTO {
    private PersonalDTO personalData;
    private String position;
    private String department;
}
