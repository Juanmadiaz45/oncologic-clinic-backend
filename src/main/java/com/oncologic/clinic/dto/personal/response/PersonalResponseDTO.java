package com.oncologic.clinic.dto.personal.response;

import com.oncologic.clinic.dto.user.response.UserResponseDTO;
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
public class PersonalResponseDTO {
    private UserResponseDTO userData;
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
