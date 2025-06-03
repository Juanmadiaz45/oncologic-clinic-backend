package com.oncologic.clinic.dto.personal;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialityDTO {

    @NotBlank(message = "The name of the specialty is mandatory")
    @Size(max = 100, message = "The name cannot exceed 100 characters.")
    private String name;

    @Size(max = 1000, message = "The description cannot exceed 1000 characters.")
    private String description;

    private Set<Long> doctorIds; // Doctor IDs to associate with this specialty
}