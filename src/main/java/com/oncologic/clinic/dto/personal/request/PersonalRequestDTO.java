package com.oncologic.clinic.dto.personal.request;


import com.oncologic.clinic.dto.UserRequestDTO;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PersonalRequestDTO extends UserRequestDTO {
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