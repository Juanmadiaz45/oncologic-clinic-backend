package com.oncologic.clinic.dto.personal.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PersonalResponseDTO {
    private Long id;
    private String idNumber;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDateTime dateOfHiring;
    private Character status;
    private Long userId;
    private Set<Long> availabilityIds;
}
