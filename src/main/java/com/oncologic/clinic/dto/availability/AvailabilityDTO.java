package com.oncologic.clinic.dto.availability;

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
public class AvailabilityDTO {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Set<Long> personalIds; // Associated staff IDs
    private Long statusId; // 👈 Changed: Now only requires the state ID
}