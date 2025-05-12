package com.oncologic.clinic.service.availability;

import com.oncologic.clinic.dto.availability.StatusDTO;
import com.oncologic.clinic.dto.availability.response.StatusResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.availability.Status;
import com.oncologic.clinic.exception.runtime.availability.StatusNotFoundException;
import com.oncologic.clinic.mapper.availability.StatusMapper;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.repository.availability.StatusRepository;
import com.oncologic.clinic.service.availability.impl.StatusServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private StatusMapper statusMapper;

    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private StatusServiceImpl statusService;

    private Status status;
    private StatusResponseDTO statusResponseDTO;
    private StatusDTO statusDTO;
    private Availability availability;

    @BeforeEach
    void setUp() {
        availability = new Availability();
        availability.setId(1L);

        status = new Status();
        status.setId(1L);
        status.setName("Disponible");
        status.setAvailability(availability);

        statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setId(1L);
        statusResponseDTO.setName("Disponible");

        statusDTO = new StatusDTO();
        statusDTO.setName("Disponible");
        statusDTO.setAvailabilityId(1L);
    }

    @Test
    void getStatusById_ShouldReturnStatusDTO_WhenIdExists() {
        // Arrange
        when(statusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(statusMapper.toDto(status)).thenReturn(statusResponseDTO);

        // Act
        StatusResponseDTO result = statusService.getStatusById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Disponible", result.getName());
        verify(statusRepository, times(1)).findById(1L);
        verify(statusMapper, times(1)).toDto(status);
    }

    @Test
    void getStatusById_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(statusRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StatusNotFoundException.class, () -> {
            statusService.getStatusById(1L);
        });

        verify(statusRepository, times(1)).findById(1L);
    }

    @Test
    void getAllStatuses_ShouldReturnListOfStatusDTOs() {
        // Arrange
        Status status2 = new Status(2L, "Ocupado", availability);
        StatusResponseDTO statusResponseDTO2 = new StatusResponseDTO();
        statusResponseDTO2.setId(2L);
        statusResponseDTO2.setName("Ocupado");

        when(statusRepository.findAll()).thenReturn(Arrays.asList(status, status2));
        when(statusMapper.toDto(status)).thenReturn(statusResponseDTO);
        when(statusMapper.toDto(status2)).thenReturn(statusResponseDTO2);

        // Act
        List<StatusResponseDTO> result = statusService.getAllStatuses();

        // Assert
        assertEquals(2, result.size());
        verify(statusRepository, times(1)).findAll();
        verify(statusMapper, times(1)).toDto(status);
        verify(statusMapper, times(1)).toDto(status2);
    }

    @Test
    void createStatus_ShouldSaveAndReturnStatusDTO() {
        // Arrange
        when(statusMapper.toEntity(statusDTO)).thenReturn(status);
        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));
        when(statusRepository.save(status)).thenReturn(status);
        when(statusMapper.toDto(status)).thenReturn(statusResponseDTO);

        // Act
        StatusResponseDTO result = statusService.createStatus(statusDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Disponible", result.getName());
        verify(statusRepository, times(1)).save(status);
        verify(statusMapper, times(1)).toEntity(statusDTO);
        verify(statusMapper, times(1)).toDto(status);
    }

    @Test
    void updateStatus_ShouldUpdateAndReturnStatusDTO_WhenIdExists() {
        // Arrange
        Status updatedStatus = new Status(1L, "En Reunión", availability);
        StatusResponseDTO updatedResponseDTO = new StatusResponseDTO();
        updatedResponseDTO.setId(1L);
        updatedResponseDTO.setName("En Reunión");

        when(statusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));
        when(statusRepository.save(any(Status.class))).thenReturn(updatedStatus);
        when(statusMapper.toDto(updatedStatus)).thenReturn(updatedResponseDTO);

        // Act
        StatusResponseDTO result = statusService.updateStatus(1L, statusDTO);

        // Assert
        assertNotNull(result);
        assertEquals("En Reunión", result.getName());
        verify(statusRepository, times(1)).findById(1L);
        verify(statusRepository, times(1)).save(any(Status.class));
    }

    @Test
    void updateStatus_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(statusRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StatusNotFoundException.class, () -> {
            statusService.updateStatus(99L, statusDTO);
        });

        verify(statusRepository, times(1)).findById(99L);
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
    void deleteStatus_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(statusRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(StatusNotFoundException.class, () -> {
            statusService.deleteStatus(1L);
        });

        verify(statusRepository, times(1)).existsById(1L);
        verify(statusRepository, never()).deleteById(any());
    }
}