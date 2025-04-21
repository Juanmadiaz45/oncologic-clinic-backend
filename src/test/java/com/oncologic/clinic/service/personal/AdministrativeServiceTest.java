package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.registration.RegisterAdministrativeDTO;
import com.oncologic.clinic.entity.personal.Administrative;
import com.oncologic.clinic.entity.user.User;
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

    @InjectMocks
    private AdministrativeServiceImpl administrativeService;

    @Test
    public void registerAdministrative_WhenValidData_ShouldCreateAdministrativeAndUser() {
        // Arrange
        RegisterAdministrativeDTO dto = new RegisterAdministrativeDTO();
        dto.setUsername("adminUser");
        dto.setPassword("password123");
        dto.setIdNumber("ID12345");
        dto.setName("John");
        dto.setLastname("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhoneNumber("123456789");
        dto.setPosition("Manager");
        dto.setDepartment("HR");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername(dto.getUsername());

        Administrative expectedAdmin = new Administrative();
        expectedAdmin.setUser(mockUser);
        expectedAdmin.setIdNumber(dto.getIdNumber());
        expectedAdmin.setName(dto.getName());
        expectedAdmin.setLastName(dto.getLastname());
        expectedAdmin.setEmail(dto.getEmail());
        expectedAdmin.setPhoneNumber(dto.getPhoneNumber());
        expectedAdmin.setPosition(dto.getPosition());
        expectedAdmin.setDepartment(dto.getDepartment());
        expectedAdmin.setDateOfHiring(LocalDateTime.now());
        expectedAdmin.setStatus('A');

        when(userService.createUser(dto)).thenReturn(mockUser);
        when(administrativeRepository.save(any(Administrative.class))).thenReturn(expectedAdmin);

        // Act
        Administrative result = administrativeService.registerAdministrative(dto);

        // Assert
        assertNotNull(result);
        assertEquals(mockUser, result.getUser());
        assertEquals(dto.getIdNumber(), result.getIdNumber());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getLastname(), result.getLastName());
        assertEquals(dto.getEmail(), result.getEmail());
        assertEquals(dto.getPhoneNumber(), result.getPhoneNumber());
        assertEquals(dto.getPosition(), result.getPosition());
        assertEquals(dto.getDepartment(), result.getDepartment());
        assertEquals('A', result.getStatus());
        assertNotNull(result.getDateOfHiring());

        verify(userService, times(1)).createUser(dto);
        verify(administrativeRepository, times(1)).save(any(Administrative.class));
    }

    @Test
    void registerAdministrative_WhenUserServiceThrowsException_ShouldPropagateException() {
        // Arrange
        RegisterAdministrativeDTO dto = new RegisterAdministrativeDTO();
        dto.setUsername("failUser");

        when(userService.createUser(dto)).thenThrow(new RuntimeException("User creation failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> administrativeService.registerAdministrative(dto));

        assertEquals("User creation failed", exception.getMessage());
        verify(userService).createUser(dto);
        verify(administrativeRepository, never()).save(any());
    }

    @Test
    void registerAdministrative_WhenRepositoryReturnsNull_ShouldReturnNull() {
        // Arrange
        RegisterAdministrativeDTO dto = new RegisterAdministrativeDTO();
        dto.setUsername("admin123");
        dto.setPassword("123");
        dto.setRoleIds(Set.of(1L));
        dto.setIdNumber("123");
        dto.setName("Ana");
        dto.setLastname("Lopez");
        dto.setEmail("ana@example.com");
        dto.setPhoneNumber("321");
        dto.setPosition("Asistente");
        dto.setDepartment("Contabilidad");

        User mockUser = new User();
        mockUser.setUsername("admin123");

        when(userService.createUser(dto)).thenReturn(mockUser);
        when(administrativeRepository.save(any(Administrative.class))).thenReturn(null);

        // Act
        Administrative result = administrativeService.registerAdministrative(dto);

        // Assert
        assertNull(result);
        verify(userService).createUser(dto);
        verify(administrativeRepository).save(any(Administrative.class));
    }
}
