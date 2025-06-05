package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.registration.RegisterDoctorDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.mapper.personal.DoctorMapper;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.impl.DoctorServiceImpl;
import com.oncologic.clinic.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserService userService;

    @Mock
    private SpecialityRepository specialityRepository;

    @Mock
    private DoctorMapper doctorMapper;

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
        doctor.setSpecialities(new HashSet<>());
        return doctor;
    }

    @Test
    void registerDoctor_WhenSpecialitiesProvided_ShouldAssignSpecialities() {
        // Arrange
        RegisterDoctorDTO dto = getBaseDTO();
        dto.setSpecialityIds(Set.of(10L, 20L));

        User user = getMockUser();
        Doctor savedDoctor = getMockDoctor(user);

        Speciality spec1 = new Speciality();
        spec1.setId(10L);
        spec1.setDoctors(new HashSet<>());

        Speciality spec2 = new Speciality();
        spec2.setId(20L);
        spec2.setDoctors(new HashSet<>());

        when(userService.createUser(dto)).thenReturn(user);
        when(doctorRepository.save(any())).thenReturn(savedDoctor);
        when(specialityRepository.findAllById(dto.getSpecialityIds()))
                .thenReturn(List.of(spec1, spec2));

        // Act
        Doctor result = doctorService.registerDoctor(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Carlos", result.getName());
        assertEquals(user, result.getUser());
        assertEquals(2, result.getSpecialities().size());

        assertTrue(spec1.getDoctors().contains(result));
        assertTrue(spec2.getDoctors().contains(result));
    }

    @Test
    void registerDoctor_WhenNoSpecialitiesProvided_ShouldNotAssignAny() {
        // Arrange
        RegisterDoctorDTO dto = getBaseDTO(); // no specialityIds set
        User user = getMockUser();
        Doctor savedDoctor = getMockDoctor(user);

        when(userService.createUser(dto)).thenReturn(user);
        when(doctorRepository.save(any())).thenReturn(savedDoctor);

        // Act
        Doctor result = doctorService.registerDoctor(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Carlos", result.getName());
        assertTrue(result.getSpecialities().isEmpty());
        verify(specialityRepository, never()).findAllById(any());
    }
}
