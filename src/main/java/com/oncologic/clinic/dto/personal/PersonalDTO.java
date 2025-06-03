package com.oncologic.clinic.dto.personal;

import com.oncologic.clinic.dto.user.UserDTO;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDTO {

    @Valid
    private UserDTO userData;

    @Size(max = 20, message = "Identification number cannot exceed 20 characters")
    private String idNumber;

    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @Email(message = "Email format is invalid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Pattern(regexp = "^[0-9+\\-\\s]+$", message = "Phone number contains invalid characters")
    @Size(max = 20, message = "Phone number cannot exceed 20 characters")
    private String phoneNumber;

    private LocalDateTime dateOfHiring;

    private Character status;

    private Set<Long> availabilityIds;
}