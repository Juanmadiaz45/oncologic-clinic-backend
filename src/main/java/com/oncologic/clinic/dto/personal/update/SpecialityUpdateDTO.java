package com.oncologic.clinic.dto.personal.update;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityUpdateDTO {
    private String name;
    private String description;
}