package com.oncologic.clinic.dto.personal.response;

import lombok.Data;

@Data
public class SpecialityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Long doctorId;
}