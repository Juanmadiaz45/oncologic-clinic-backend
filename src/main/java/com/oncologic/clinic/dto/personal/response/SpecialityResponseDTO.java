package com.oncologic.clinic.dto.personal.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private Set<Long> doctorIds;
}