package com.oncologic.clinic.dto.personal.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministrativeResponseDTO {
    private PersonalResponseDTO personalData;
    private String position;
    private String department;
}
