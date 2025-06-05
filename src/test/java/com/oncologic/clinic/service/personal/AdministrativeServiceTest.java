/*package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.request.PersonalRequestDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.dto.personal.response.PersonalResponseDTO;
import com.oncologic.clinic.dto.registration.RegisterAdministrativeDTO;
import com.oncologic.clinic.dto.user.UserDTO;
import com.oncologic.clinic.dto.user.response.UserResponseDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.mapper.personal.AdministrativeMapper;
import com.oncologic.clinic.repository.personal.AdministrativeRepository;
import com.oncologic.clinic.service.personal.impl.AdministrativeServiceImpl;
import com.oncologic.clinic.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdministrativeServiceTest {
    @Mock
    private AdministrativeRepository administrativeRepository;

    @Mock
    private UserService userService;

    @Mock
    private AdministrativeMapper administrativeMapper;

    @InjectMocks
    private AdministrativeServiceImpl administrativeService;

    @Test
    public void createAdministrative_WhenValidData_ShouldCreateAdministrativeAndUser() {
        // Arrange
        AdministrativeDTO dto = new AdministrativeDTO();
        dto.setPosition("Manager");
        dto.setDepartment("HR");

        PersonalRequestDTO personalData = new PersonalRequestDTO();
        personalData.setIdNumber("ID12345");
        personalData.setName("John");
        personalData.setLastName("Doe");
        personalData.setEmail("john.doe@example.com");
        personalData.setPhoneNumber("123456789");

        UserDTO userData = new UserDTO();
        userData.setUsername("adminUser");
        userData.setPassword("password123");
        userData.setRoleIds(Set.of(1L));

        personalData.setUserData(userData);
        dto.setPersonalData(personalData);

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(userData.getUsername());

        Administrative administrativeEntity = new Administrative();
        administrativeEntity.setUser(mockUser);
        administrativeEntity.setIdNumber(personalData.getIdNumber());
        administrativeEntity.setName(personalData.getName());
        administrativeEntity.setLastName(personalData.getLastName());
        administrativeEntity.setEmail(personalData.getEmail());
        administrativeEntity.setPhoneNumber(personalData.getPhoneNumber());
        administrativeEntity.setPosition(dto.getPosition());
        administrativeEntity.setDepartment(dto.getDepartment());
        administrativeEntity.setDateOfHiring(LocalDateTime.now());
        administrativeEntity.setStatus('A');

        // Create response DTOs with proper structure
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .id(mockUser.getId())
                .username(mockUser.getUsername())
                .build();

        PersonalResponseDTO personalResponseDTO = PersonalResponseDTO.builder()
                .userData(userResponseDTO)
                .idNumber(personalData.getIdNumber())
                .name(personalData.getName())
                .lastName(personalData.getLastName())
                .email(personalData.getEmail())
                .phoneNumber(personalData.getPhoneNumber())
                .dateOfHiring(LocalDateTime.now())
                .status('A')
                .build();

        AdministrativeResponseDTO responseDTO = AdministrativeResponseDTO.builder()
                .personalData(personalResponseDTO)
                .position(dto.getPosition())
                .department(dto.getDepartment())
                .build();

        RegisterAdministrativeDTO registerDto = new RegisterAdministrativeDTO();
        registerDto.setUsername(userData.getUsername());
        registerDto.setPassword(userData.getPassword());
        registerDto.setRoleIds(userData.getRoleIds());
        registerDto.setIdNumber(personalData.getIdNumber());
        registerDto.setName(personalData.getName());
        registerDto.setLastname(personalData.getLastName());
        registerDto.setEmail(personalData.getEmail());
        registerDto.setPhoneNumber(personalData.getPhoneNumber());
        registerDto.setPosition(dto.getPosition());
        registerDto.setDepartment(dto.getDepartment());

        when(userService.createUser(any(RegisterAdministrativeDTO.class))).thenReturn(mockUser);
        when(administrativeRepository.save(any(Administrative.class))).thenReturn(administrativeEntity);
        when(administrativeMapper.toDto(administrativeEntity)).thenReturn(responseDTO);

        // Act
        AdministrativeResponseDTO result = administrativeService.createAdministrative(dto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getPersonalData());
        assertEquals(personalData.getIdNumber(), result.getPersonalData().getIdNumber());
        assertEquals(personalData.getName(), result.getPersonalData().getName());
        assertEquals(personalData.getLastName(), result.getPersonalData().getLastName());
        assertEquals(personalData.getEmail(), result.getPersonalData().getEmail());
        assertEquals(personalData.getPhoneNumber(), result.getPersonalData().getPhoneNumber());
        assertEquals(dto.getPosition(), result.getPosition());
        assertEquals(dto.getDepartment(), result.getDepartment());

        verify(userService).createUser(any(RegisterAdministrativeDTO.class));
        verify(administrativeRepository).save(any(Administrative.class));
        verify(administrativeMapper).toDto(administrativeEntity);
    }

    @Test
    void createAdministrative_WhenUserServiceThrowsException_ShouldPropagateException() {
        // Arrange
        AdministrativeDTO dto = new AdministrativeDTO();
        dto.setPosition("Manager");

        PersonalRequestDTO personalData = new PersonalRequestDTO();
        UserDTO userData = new UserDTO();
        userData.setUsername("failUser");
        personalData.setUserData(userData);
        dto.setPersonalData(personalData);

        when(userService.createUser(any(RegisterAdministrativeDTO.class)))
                .thenThrow(new RuntimeException("User creation failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> administrativeService.createAdministrative(dto));

        assertEquals("User creation failed", exception.getMessage());
        verify(userService).createUser(any(RegisterAdministrativeDTO.class));
        verify(administrativeRepository, never()).save(any());
    }

    @Test
    void createAdministrative_WhenRepositoryReturnsNull_ShouldReturnNull() {
        // Arrange
        AdministrativeDTO dto = new AdministrativeDTO();
        dto.setPosition("Asistente");
        dto.setDepartment("Contabilidad");

        PersonalRequestDTO personalData = new PersonalRequestDTO();
        personalData.setIdNumber("123");
        personalData.setName("Ana");
        personalData.setLastName("Lopez");
        personalData.setEmail("ana@example.com");
        personalData.setPhoneNumber("321");

        UserDTO userData = new UserDTO();
        userData.setUsername("admin123");
        userData.setPassword("123");
        userData.setRoleIds(Set.of(1L));

        personalData.setUserData(userData);
        dto.setPersonalData(personalData);

        User mockUser = new User();
        mockUser.setId(2L);
        mockUser.setUsername(userData.getUsername());

        when(userService.createUser(any(RegisterAdministrativeDTO.class))).thenReturn(mockUser);
        when(administrativeRepository.save(any(Administrative.class))).thenReturn(null);

        // Act
        AdministrativeResponseDTO result = administrativeService.createAdministrative(dto);

        // Assert
        assertNull(result);
        verify(userService).createUser(any(RegisterAdministrativeDTO.class));
        verify(administrativeRepository).save(any(Administrative.class));
    }

}*/
