package com.oncologic.clinic.dto.personal.request;


import com.oncologic.clinic.dto.user.request.UserRequestDTO;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PersonalRequestDTO {
    private UserRequestDTO userData;
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