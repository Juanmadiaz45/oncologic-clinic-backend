package com.oncologic.clinic.dto.personal.request;


import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PersonalRequestDTO {
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