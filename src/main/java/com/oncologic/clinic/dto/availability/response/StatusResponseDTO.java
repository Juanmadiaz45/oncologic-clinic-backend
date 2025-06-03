package com.oncologic.clinic.dto.availability.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponseDTO {
    private Long id;
    private String name;
    private Set<Long> availabilityIds;
}