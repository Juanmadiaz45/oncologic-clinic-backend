package com.oncologic.clinic.service.availability;

import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.availability.Status;
import com.oncologic.clinic.repository.availability.StatusRepository;
import com.oncologic.clinic.service.availability.impl.StatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private StatusServiceImpl statusService;

    private Status status;
    private Availability availability;

    @BeforeEach
    void setUp() {
        availability = new Availability();
        availability.setId(1L);

        status = new Status();
        status.setId(1L);
        status.setName("Disponible");
        status.setAvailability(availability);
    }

    @Test
    void getStatusById_ShouldReturnStatus_WhenIdExists() {
        // Arrange
        when(statusRepository.findById(1L)).thenReturn(Optional.of(status));

        // Act
        Status result = statusService.getStatusById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Disponible", result.getName());
        verify(statusRepository, times(1)).findById(1L);
    }

    @Test
    void getStatusById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Status result = statusService.getStatusById(1L);

        // Assert
        assertNull(result);
        verify(statusRepository, times(1)).findById(1L);
    }

    @Test
    void getAllStatuses_ShouldReturnListOfStatuses() {
        // Arrange
        Status status2 = new Status(2L, "Ocupado", availability);
        when(statusRepository.findAll()).thenReturn(Arrays.asList(status, status2));

        // Act
        List<Status> result = statusService.getAllStatuses();

        // Assert
        assertEquals(2, result.size());
        verify(statusRepository, times(1)).findAll();
    }

    @Test
    void createStatus_ShouldSaveAndReturnStatus() {
        // Arrange
        when(statusRepository.save(status)).thenReturn(status);

        // Act
        Status result = statusService.createStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals("Disponible", result.getName());
        verify(statusRepository, times(1)).save(status);
    }

    @Test
    void updateStatus_ShouldUpdateAndReturnStatus_WhenIdExists() {
        // Arrange
        Status updatedStatus = new Status(1L, "En Reunión", availability);
        when(statusRepository.existsById(1L)).thenReturn(true);
        when(statusRepository.save(updatedStatus)).thenReturn(updatedStatus);

        // Act
        Status result = statusService.updateStatus(updatedStatus);

        // Assert
        assertNotNull(result);
        assertEquals("En Reunión", result.getName());
        verify(statusRepository, times(1)).existsById(1L);
        verify(statusRepository, times(1)).save(updatedStatus);
    }

    @Test
    void updateStatus_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        Status updatedStatus = new Status(99L, "Sin Asignar", availability);
        when(statusRepository.existsById(99L)).thenReturn(false);

        // Act
        Status result = statusService.updateStatus(updatedStatus);

        // Assert
        assertNull(result);
        verify(statusRepository, times(1)).existsById(99L);
        verify(statusRepository, never()).save(any());
    }

    @Test
    void deleteStatus_ShouldDelete_WhenIdExists() {
        // Arrange
        when(statusRepository.existsById(1L)).thenReturn(true);
        doNothing().when(statusRepository).deleteById(1L);

        // Act
        statusService.deleteStatus(1L);

        // Assert
        verify(statusRepository, times(1)).existsById(1L);
        verify(statusRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteStatus_ShouldNotDelete_WhenIdDoesNotExist() {
        // Arrange
        when(statusRepository.existsById(1L)).thenReturn(false);

        // Act
        statusService.deleteStatus(1L);

        // Assert
        verify(statusRepository, times(1)).existsById(1L);
        verify(statusRepository, never()).deleteById(any());
    }
}
