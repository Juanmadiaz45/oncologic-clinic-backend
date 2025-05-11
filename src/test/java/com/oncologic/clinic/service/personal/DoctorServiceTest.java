/*package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.SpecialityDTO;
import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.impl.DoctorServiceImpl;
import com.oncologic.clinic.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserService userService;

    @Mock
    private SpecialityService specialityService;

    @Mock
    private SpecialityRepository specialityRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private RegisterDoctorDTO getBaseDTO() {
        RegisterDoctorDTO dto = new RegisterDoctorDTO();
        dto.setUsername("doc123");
        dto.setPassword("password");
        dto.setRoleIds(Set.of(1L));
        dto.setIdNumber("111");
        dto.setName("Carlos");
        dto.setLastname("Lopez");
        dto.setEmail("carlos@example.com");
        dto.setPhoneNumber("321321321");
        dto.setMedicalLicenseNumber("MLN-123");
        return dto;
    }

    private User getMockUser() {
        User user = new User();
        user.setUsername("doc123");
        return user;
    }

    private Doctor getMockDoctor(User user) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setName("Carlos");
        return doctor;
    }

    @Test
    void registerDoctor_WhenSpecialityIdProvided_ShouldAssignSpeciality() {
        // Arrange
        RegisterDoctorDTO dto = getBaseDTO();
        dto.setSelectedSpecialityId(10L);

        User user = getMockUser();
        Doctor doctor = getMockDoctor(user);
        Speciality speciality = new Speciality();

        when(userService.createUser(dto)).thenReturn(user);
        when(doctorRepository.save(any())).thenReturn(doctor);
        when(specialityService.getSpecialityById(10L)).thenReturn(Optional.of(speciality));

        // Act
        Doctor result = doctorService.registerDoctor(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Carlos", result.getName());
        assertEquals(user, result.getUser());

        verify(specialityRepository).save(speciality);
        assertEquals(doctor, speciality.getDoctor());
    }

    @Test
    void registerDoctor_WhenNewSpecialityProvided_ShouldRegisterNewSpeciality() {
        // Arrange
        RegisterDoctorDTO dto = getBaseDTO();
        dto.setNewSpecialityName("Cardiología");
        dto.setNewSpecialityDescription("Corazón y sistema cardiovascular");

        User user = getMockUser();
        Doctor doctor = getMockDoctor(user);

        when(userService.createUser(dto)).thenReturn(user);
        when(doctorRepository.save(any())).thenReturn(doctor);

        // Act
        Doctor result = doctorService.registerDoctor(dto);

        // Assert
        assertNotNull(result);
        verify(specialityService).registerSpeciality(any(SpecialityDTO.class), eq(doctor));
        verify(specialityRepository, never()).save(any());
    }

    @Test
    void registerDoctor_WhenNoSpecialityProvided_ShouldNotRegisterOrAssignSpeciality() {
        // Arrange
        RegisterDoctorDTO dto = getBaseDTO();

        User user = getMockUser();
        Doctor doctor = getMockDoctor(user);

        when(userService.createUser(dto)).thenReturn(user);
        when(doctorRepository.save(any())).thenReturn(doctor);

        // Act
        Doctor result = doctorService.registerDoctor(dto);

        // Assert
        assertNotNull(result);
        verify(specialityService, never()).registerSpeciality(any(), any());
        verify(specialityRepository, never()).save(any());
    }

    @Test
    void registerDoctor_WhenSpecialityIdNotFound_ShouldNotFail() {
        // Arrange
        RegisterDoctorDTO dto = getBaseDTO();
        dto.setSelectedSpecialityId(999L);

        User user = getMockUser();
        Doctor doctor = getMockDoctor(user);

        when(userService.createUser(dto)).thenReturn(user);
        when(doctorRepository.save(any())).thenReturn(doctor);
        when(specialityService.getSpecialityById(999L)).thenReturn(Optional.empty());

        // Act
        Doctor result = doctorService.registerDoctor(dto);

        // Assert
        assertNotNull(result);
        verify(specialityRepository, never()).save(any());
        verify(specialityService).getSpecialityById(999L);
    }
}*/
