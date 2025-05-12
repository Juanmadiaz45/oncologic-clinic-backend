package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.PatientRequestDTO;
import com.oncologic.clinic.dto.patient.response.PatientResponseDTO;
import com.oncologic.clinic.dto.patient.update.PatientUpdateDTO;
import com.oncologic.clinic.dto.registration.RegisterPatientDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.exception.runtime.patient.PatientNotFoundException;
import com.oncologic.clinic.mapper.patient.PatientMapper;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.service.patient.impl.PatientServiceImpl;
import com.oncologic.clinic.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserService userService;

    @Mock
    private MedicalHistoryService medicalHistoryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private RegisterPatientDTO getMockRegisterPatientDTO() {
        UserDTO userDTO = UserDTO.builder()
                .username("juanito")
                .password("123456")
                .roleIds(Set.of(2L))
                .build();

        return RegisterPatientDTO.builder()
                .userData(userDTO)
                .name("Juan Pérez")
                .birthDate("1990-05-15")
                .gender('M')
                .address("Calle Falsa 123")
                .phoneNumber("555123456")
                .email("juan.perez@email.com")
                .currentHealthStatus("Salud estable")
                .build();
    }

    private PatientRequestDTO getMockPatientRequestDTO() {
        UserDTO userDTO = UserDTO.builder()
                .username("juanito")
                .password("123456")
                .roleIds(Set.of(2L))
                .build();

        return PatientRequestDTO.builder()
                .userData(userDTO)
                .name("Juan Pérez")
                .birthDate("1990-05-15")
                .gender('M')
                .address("Calle Falsa 123")
                .phoneNumber("555123456")
                .email("juan.perez@email.com")
                .currentHealthStatus("Salud estable")
                .build();
    }

    private PatientUpdateDTO getMockPatientUpdateDTO() {
        return PatientUpdateDTO.builder()
                .name("Juan Pérez Actualizado")
                .birthDate("1990-05-15")
                .gender('M')
                .address("Calle Falsa 123")
                .phoneNumber("555123456")
                .email("juan.perez@email.com")
                .build();
    }

    private User getMockUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("juanito");
        return user;
    }

    private UserResponseDTO getMockUserResponseDTO() {
        return UserResponseDTO.builder()
                .id(1L)
                .username("juanito")
                .build();
    }

    private MedicalHistory getMockMedicalHistory() {
        MedicalHistory history = new MedicalHistory();
        history.setId(1L);
        history.setCurrentHealthStatus("Salud estable");
        return history;
    }

    private Patient getMockPatient() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("Juan Pérez");
        patient.setBirthdate(LocalDateTime.now());
        patient.setGender('M');
        patient.setAddress("Calle Falsa 123");
        patient.setPhoneNumber("555123456");
        patient.setEmail("juan.perez@email.com");
        return patient;
    }

    private PatientResponseDTO getMockPatientResponseDTO() {
        return PatientResponseDTO.builder()
                .id(1L)
                .name("Juan Pérez")
                .birthDate("1990-05-15")
                .gender('M')
                .address("Calle Falsa 123")
                .phoneNumber("555123456")
                .email("juan.perez@email.com")
                .build();
    }

    @Test
    void registerPatient_WhenValidData_ShouldRegisterAndReturnPatient() {
        // Arrange
        RegisterPatientDTO dto = getMockRegisterPatientDTO();
        UserResponseDTO mockUserResponse = getMockUserResponseDTO();
        User mockUser = getMockUser();
        MedicalHistory mockHistory = getMockMedicalHistory();
        Patient mockSavedPatient = getMockPatient();

        when(userService.createUser(dto.getUserData())).thenReturn(mockUserResponse);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(patientRepository.save(any(Patient.class))).thenReturn(mockSavedPatient);
        when(medicalHistoryService.registerMedicalHistory(any(Patient.class), eq("Salud estable")))
                .thenReturn(mockHistory);

        // Act
        Patient result = patientService.registerPatient(dto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userService).createUser(dto.getUserData());
        verify(userRepository).findById(1L);
        verify(patientRepository, times(2)).save(any(Patient.class));
        verify(medicalHistoryService).registerMedicalHistory(any(Patient.class), eq("Salud estable"));
    }

    @Test
    void registerPatient_WhenInvalidDateFormat_ShouldThrowException() {
        // Arrange
        RegisterPatientDTO dto = getMockRegisterPatientDTO();
        dto.setBirthDate("15-05-1990");

        UserResponseDTO mockUserResponse = getMockUserResponseDTO();
        when(userService.createUser(dto.getUserData())).thenReturn(mockUserResponse);

        User mockUser = getMockUser();
        when(userRepository.findById(mockUserResponse.getId())).thenReturn(Optional.of(mockUser));

        // Act & Assert
        Exception exception = assertThrows(DateTimeParseException.class, () ->
                patientService.registerPatient(dto));

        String expectedMessagePart = "could not be parsed";
        assertTrue(exception.getMessage().contains(expectedMessagePart));
    }

    @Test
    void getPatientById_WhenPatientExists_ReturnsPatientDTO() {
        // Arrange
        Long patientId = 1L;
        Patient patient = getMockPatient();
        PatientResponseDTO responseDTO = getMockPatientResponseDTO();

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(responseDTO);

        // Act
        PatientResponseDTO result = patientService.getPatientById(patientId);

        // Assert
        assertNotNull(result);
        assertEquals(patientId, result.getId());
        assertEquals("Juan Pérez", result.getName());

        verify(patientRepository, times(1)).findById(patientId);
        verify(patientMapper, times(1)).toDto(patient);
    }

    @Test
    void getPatientById_WhenPatientDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 99L;
        when(patientRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(PatientNotFoundException.class, () ->
                patientService.getPatientById(nonExistentId));

        assertEquals("Patient not found with ID: " + nonExistentId, exception.getMessage()); // Cambiado a "ID"

        verify(patientRepository, times(1)).findById(nonExistentId);
        verify(patientMapper, never()).toDto(any());
    }

    @Test
    void getAllPatients_WhenPatientsExist_ReturnsPatientDTOList() {
        // Arrange
        Patient patient1 = getMockPatient();
        Patient patient2 = getMockPatient();
        patient2.setId(2L);
        patient2.setName("María García");

        PatientResponseDTO dto1 = getMockPatientResponseDTO();
        PatientResponseDTO dto2 = getMockPatientResponseDTO();
        dto2.setId(2L);
        dto2.setName("María García");

        when(patientRepository.findAll()).thenReturn(List.of(patient1, patient2));
        when(patientMapper.toDto(patient1)).thenReturn(dto1);
        when(patientMapper.toDto(patient2)).thenReturn(dto2);

        // Act
        List<PatientResponseDTO> result = patientService.getAllPatients();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Juan Pérez", result.get(0).getName());
        assertEquals("María García", result.get(1).getName());

        verify(patientRepository, times(1)).findAll();
        verify(patientMapper, times(1)).toDto(patient1);
        verify(patientMapper, times(1)).toDto(patient2);
    }

    @Test
    void getAllPatients_WhenNoPatientsExist_ReturnsEmptyList() {
        // Arrange
        when(patientRepository.findAll()).thenReturn(List.of());

        // Act
        List<PatientResponseDTO> result = patientService.getAllPatients();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(patientRepository, times(1)).findAll();
        verify(patientMapper, never()).toDto(any());
    }

    @Test
    void createPatient_WhenValidData_ReturnsPatientDTO() {
        // Arrange
        PatientRequestDTO requestDTO = getMockPatientRequestDTO();
        UserResponseDTO userResponse = getMockUserResponseDTO();
        Patient patient = getMockPatient();
        Patient savedPatient = getMockPatient();
        PatientResponseDTO responseDTO = getMockPatientResponseDTO();

        when(userService.createUser(requestDTO.getUserData())).thenReturn(userResponse);

        User mockUser = getMockUser();
        when(userService.getUserEntityById(userResponse.getId())).thenReturn(mockUser);

        when(patientMapper.toEntity(requestDTO)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(savedPatient);
        when(patientMapper.toDto(savedPatient)).thenReturn(responseDTO);
        when(patientRepository.findById(savedPatient.getId())).thenReturn(Optional.of(savedPatient));

        // Act
        PatientResponseDTO result = patientService.createPatient(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(userService, times(1)).createUser(requestDTO.getUserData());
        verify(userService, times(1)).getUserEntityById(userResponse.getId()); // Cambia esta verificación
        verify(patientMapper, times(1)).toEntity(requestDTO);
        verify(patientRepository, times(1)).save(patient);
        verify(patientMapper, times(1)).toDto(savedPatient);
    }

    @Test
    void updatePatient_WhenPatientExists_ReturnsUpdatedPatientDTO() {
        // Arrange
        Long patientId = 1L;
        PatientUpdateDTO updateDTO = getMockPatientUpdateDTO();
        Patient existingPatient = getMockPatient();
        Patient updatedPatient = getMockPatient();
        updatedPatient.setName("Juan Pérez Actualizado");

        PatientResponseDTO responseDTO = getMockPatientResponseDTO();
        responseDTO.setName("Juan Pérez Actualizado");

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(existingPatient)).thenReturn(updatedPatient);
        when(patientMapper.toDto(updatedPatient)).thenReturn(responseDTO);

        // Act
        PatientResponseDTO result = patientService.updatePatient(patientId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(patientId, result.getId());
        assertEquals("Juan Pérez Actualizado", result.getName());

        verify(patientRepository, times(1)).findById(patientId);
        verify(patientRepository, times(1)).save(existingPatient);
        verify(patientMapper, times(1)).toDto(updatedPatient);
    }
}