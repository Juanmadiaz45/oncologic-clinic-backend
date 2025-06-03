package com.oncologic.clinic.dto.availability.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilityResponseDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<Long> personalIds; // Associated staff IDs
    private StatusResponseDTO status; // 👈 Changed: Now it's a single DTO, not a Set
}