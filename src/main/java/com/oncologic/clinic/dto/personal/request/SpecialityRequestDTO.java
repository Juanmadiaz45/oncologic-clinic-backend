package com.oncologic.clinic.dto.personal.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityRequestDTO {
    private String name;
    private String description;
    private Set<Long> doctorIds;
}
