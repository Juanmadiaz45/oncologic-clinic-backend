package com.oncologic.clinic.dto.personal.request;

import lombok.Data;

@Data
public class SpecialityRequestDTO {
    private String name;
    private String description;
    private Long doctorId;
}