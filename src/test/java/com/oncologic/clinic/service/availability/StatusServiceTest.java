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

import java.util.*;

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
        status = new Status();
        status.setId(1L);
        status.setName("Disponible");
        statusResponseDTO = new StatusResponseDTO();
        statusResponseDTO.setId(1L);
        statusResponseDTO.setName("Disponible");


        statusDTO = new StatusDTO();
        statusDTO.setName("Disponible");

        availability = new Availability();

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
        assertThrows(StatusNotFoundException.class, () -> statusService.getStatusById(1L));

        verify(statusRepository, times(1)).findById(1L);
    }

    @Test
    void getAllStatuses_ShouldReturnListOfStatusDTOs() {
        // Arrange
        var availabilitiesList = new ArrayList<Availability>();
        availabilitiesList.add(availability);
        Status status2 = new Status(2L, "Ocupado", availabilitiesList);
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
        when(statusRepository.save(status)).thenReturn(status);
        when(statusMapper.toDto(status)).thenReturn(statusResponseDTO);

        // Act
        StatusResponseDTO result = statusService.createStatus(statusDTO);

        // Assert
        assertNotNull(result);
        verify(availabilityRepository, never()).findById(any()); // No longer needed
    }

    @Test
    void updateStatus_ShouldUpdateAndReturnStatusDTO_WhenIdExists() {
        // Arrange
        Status updatedStatus = new Status(1L, "In Meeting", null);
        StatusResponseDTO updatedResponseDTO = new StatusResponseDTO(1L, "In Meeting", new HashSet<>());

        when(statusRepository.findById(1L)).thenReturn(Optional.of(status));
        when(statusRepository.save(any(Status.class))).thenReturn(updatedStatus);
        when(statusMapper.toDto(updatedStatus)).thenReturn(updatedResponseDTO);

        // Act
        StatusResponseDTO result = statusService.updateStatus(1L, statusDTO);

        // Assert
        assertEquals("In Meeting", result.getName());
        verify(availabilityRepository, never()).findById(any()); // No longer needed
    }

    @Test
    void updateStatus_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        Long nonExistentId = 99L;
        when(statusRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        StatusNotFoundException exception = assertThrows(StatusNotFoundException.class,
                () -> statusService.updateStatus(nonExistentId, statusDTO));

        assertEquals("Status not found with ID " + nonExistentId, exception.getMessage());
        verify(statusRepository).findById(nonExistentId);
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
        assertThrows(StatusNotFoundException.class, () -> statusService.deleteStatus(1L));

        verify(statusRepository, times(1)).existsById(1L);
        verify(statusRepository, never()).deleteById(any());
    }
}