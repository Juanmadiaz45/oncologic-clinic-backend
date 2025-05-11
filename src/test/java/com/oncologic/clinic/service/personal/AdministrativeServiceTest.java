package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.AdministrativeDTO;
import com.oncologic.clinic.dto.personal.response.AdministrativeResponseDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.mapper.PersonalMapper;
import com.oncologic.clinic.repository.personal.AdministrativeRepository;
import com.oncologic.clinic.service.personal.impl.AdministrativeServiceImpl;
import com.oncologic.clinic.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

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
    private PersonalMapper personalMapper;

    @InjectMocks
    private AdministrativeServiceImpl administrativeService;

    @Test
    public void createAdministrative_WhenValidData_ShouldCreateAdministrativeAndUser() {
        // Arrange
        AdministrativeDTO dto = new AdministrativeDTO();
        dto.setUsername("adminUser");
        dto.setPassword("password123");
        dto.setIdNumber("ID12345");
        dto.setName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhoneNumber("123456789");
        dto.setPosition("Manager");
        dto.setDepartment("HR");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(dto.getUsername());

        Administrative administrativeEntity = new Administrative();
        administrativeEntity.setUser(mockUser);
        administrativeEntity.setIdNumber(dto.getIdNumber());
        administrativeEntity.setName(dto.getName());
        administrativeEntity.setLastName(dto.getLastName());
        administrativeEntity.setEmail(dto.getEmail());
        administrativeEntity.setPhoneNumber(dto.getPhoneNumber());
        administrativeEntity.setPosition(dto.getPosition());
        administrativeEntity.setDepartment(dto.getDepartment());
        administrativeEntity.setDateOfHiring(LocalDateTime.now());
        administrativeEntity.setStatus('A');

        AdministrativeResponseDTO responseDTO = new AdministrativeResponseDTO();
        responseDTO.setUserId(mockUser.getId());
        responseDTO.setIdNumber(dto.getIdNumber());
        responseDTO.setName(dto.getName());
        responseDTO.setLastName(dto.getLastName());
        responseDTO.setEmail(dto.getEmail());
        responseDTO.setPhoneNumber(dto.getPhoneNumber());
        responseDTO.setPosition(dto.getPosition());
        responseDTO.setDepartment(dto.getDepartment());
        responseDTO.setStatus('A');
        responseDTO.setDateOfHiring(LocalDateTime.now());

        // Simulaciones
        when(userService.createUser(dto)).thenReturn(mockUser);
        when(personalMapper.toEntity(dto)).thenReturn(administrativeEntity);
        when(administrativeRepository.save(any(Administrative.class))).thenReturn(administrativeEntity);
        when(personalMapper.toDto(administrativeEntity)).thenReturn(responseDTO);

        // Act
        AdministrativeResponseDTO result = administrativeService.createAdministrative(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getIdNumber(), result.getIdNumber());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getLastName(), result.getLastName());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(dto.getPosition(), result.getPosition());
        assertEquals(dto.getDepartment(), result.getDepartment());

        verify(userService).createUser(dto);
        verify(personalMapper).toEntity(dto);
        verify(administrativeRepository).save(any(Administrative.class));
        verify(personalMapper).toDto(administrativeEntity);
    }


    @Test
    void createAdministrative_WhenUserServiceThrowsException_ShouldPropagateException() {
        // Arrange
        AdministrativeDTO dto = new AdministrativeDTO();
        dto.setUsername("failUser");

        when(userService.createUser(dto)).thenThrow(new RuntimeException("User creation failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> administrativeService.createAdministrative(dto));

        assertEquals("User creation failed", exception.getMessage());
        verify(userService).createUser(dto);
        verify(administrativeRepository, never()).save(any());
    }


    @Test
    void createAdministrative_WhenRepositoryReturnsNull_ShouldReturnNull() {
        // Arrange
        AdministrativeDTO dto = new AdministrativeDTO();
        dto.setUsername("admin123");
        dto.setPassword("123");
        dto.setIdNumber("123");
        dto.setName("Ana");
        dto.setLastName("Lopez");
        dto.setEmail("ana@example.com");
        dto.setPhoneNumber("321");
        dto.setPosition("Asistente");
        dto.setDepartment("Contabilidad");
        dto.setDateOfHiring(LocalDateTime.now());
        dto.setStatus('A');

        User mockUser = new User();
        mockUser.setId(2L);
        mockUser.setUsername(dto.getUsername());

        when(userService.createUser(dto)).thenReturn(mockUser);
        when(administrativeRepository.save(any(Administrative.class))).thenReturn(null);

        // Act
        AdministrativeResponseDTO result = administrativeService.createAdministrative(dto);

        // Assert
        assertNull(result);
        verify(userService).createUser(dto);
        verify(administrativeRepository).save(any(Administrative.class));
    }

}
