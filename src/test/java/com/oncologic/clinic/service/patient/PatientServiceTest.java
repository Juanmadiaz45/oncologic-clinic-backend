package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.service.patient.impl.PatientServiceImpl;
import com.oncologic.clinic.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/*
@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserService userService;

    @Mock
    private MedicalHistoryService medicalHistoryService;

    @InjectMocks
    private PatientServiceImpl patientService;

    private RegisterPatientDTO getMockPatientDTO() {
        RegisterPatientDTO dto = new RegisterPatientDTO();
        dto.setUsername("juanito");
        dto.setPassword("123456");
        dto.setRoleIds(Set.of(2L));
        dto.setName("Juan Pérez");
        dto.setBirthDate("1990-05-15");
        dto.setGender('M');
        dto.setAddress("Calle Falsa 123");
        dto.setPhoneNumber("555123456");
        dto.setEmail("juan.perez@email.com");
        dto.setCurrentHealthStatus("Salud estable");
        return dto;
    }

    private User getMockUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("juanito");
        return user;
    }

    private MedicalHistory getMockMedicalHistory() {
        MedicalHistory history = new MedicalHistory();
        history.setId(1L);
        history.setCurrentHealthStatus("Salud estable");
        return history;
    }

    @Test
    void registerPatient_WhenValidData_ShouldRegisterAndReturnPatient() {
        // Arrange
        RegisterPatientDTO dto = getMockPatientDTO();
        User mockUser = getMockUser();
        MedicalHistory mockHistory = getMockMedicalHistory();
        Patient mockSavedPatient = new Patient();
        mockSavedPatient.setId(1L);

        when(userService.createUser(dto)).thenReturn(mockUser);
        when(patientRepository.save(any(Patient.class))).thenReturn(mockSavedPatient);
        when(medicalHistoryService.registerMedicalHistory(any(Patient.class), eq("Salud estable")))
                .thenReturn(mockHistory);

        // Act
        Patient result = patientService.registerPatient(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userService).createUser(dto);
        verify(patientRepository, times(2)).save(any(Patient.class));
        verify(medicalHistoryService).registerMedicalHistory(any(Patient.class), eq("Salud estable"));
    }

    @Test
    void registerPatient_WhenInvalidDateFormat_ShouldThrowException() {
        // Arrange
        RegisterPatientDTO dto = getMockPatientDTO();
        dto.setBirthDate("15-05-1990"); // formato inválido

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> patientService.registerPatient(dto));

        String expectedMessagePart = "Text '15-05-1990' could not be parsed";
        assertTrue(exception.getMessage().contains(expectedMessagePart));
    }
}


 */