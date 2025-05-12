package com.oncologic.clinic.dto.personal.request;

import com.oncologic.clinic.dto.user.UserDTO;
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
public class PersonalRequestDTO {
    private UserDTO userData;
    private String idNumber;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDateTime dateOfHiring;
    private Character status;
    private Set<Long> availabilityIds;
}