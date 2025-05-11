package com.oncologic.clinic.dto.personal.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long doctorId;
}