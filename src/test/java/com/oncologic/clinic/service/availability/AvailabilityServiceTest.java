package com.oncologic.clinic.service.availability;

import com.oncologic.clinic.dto.availability.AvailabilityDTO;
import com.oncologic.clinic.dto.availability.response.AvailabilityResponseDTO;
import com.oncologic.clinic.dto.availability.response.StatusResponseDTO;
import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.availability.Status;
import com.oncologic.clinic.exception.runtime.availability.AvailabilityNotFoundException;
import com.oncologic.clinic.mapper.availability.AvailabilityMapper;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.repository.availability.StatusRepository;
import com.oncologic.clinic.service.availability.impl.AvailabilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private AvailabilityMapper availabilityMapper;

    @Mock
    private StatusRepository statusRepository;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private Availability availability;
    private AvailabilityResponseDTO availabilityResponseDTO;
    private AvailabilityDTO availabilityDTO;

    @BeforeEach
    void setUp() {
        Status status = new Status();
        status.setId(1L);
        status.setName("Disponible");

        availability = new Availability();
        availability.setId(1L);
        availability.setStartTime(LocalDateTime.of(2025, 4, 20, 9, 0));
        availability.setEndTime(LocalDateTime.of(2025, 4, 20, 17, 0));
        availability.setStatus(status); // Added status relationship

        availabilityResponseDTO = new AvailabilityResponseDTO();
        availabilityResponseDTO.setId(1L);
        availabilityResponseDTO.setStartTime(LocalDateTime.of(2025, 4, 20, 9, 0));
        availabilityResponseDTO.setEndTime(LocalDateTime.of(2025, 4, 20, 17, 0));
        availabilityResponseDTO.setStatus(new StatusResponseDTO(1L, "Disponible", new HashSet<>())); // Added status

        availabilityDTO = new AvailabilityDTO();
        availabilityDTO.setStartTime(LocalDateTime.of(2025, 4, 20, 9, 0));
        availabilityDTO.setEndTime(LocalDateTime.of(2025, 4, 20, 17, 0));
        availabilityDTO.setStatusId(1L); // Added statusId
    }

    @Test
    void getAvailabilityById_ShouldReturnAvailabilityDTO_WhenIdExists() {
        // Arrange
        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));
        when(availabilityMapper.toDto(availability)).thenReturn(availabilityResponseDTO);

        // Act
        AvailabilityResponseDTO result = availabilityService.getAvailabilityById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(availabilityRepository, times(1)).findById(1L);
        verify(availabilityMapper, times(1)).toDto(availability);
    }

    @Test
    void getAvailabilityById_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(availabilityRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AvailabilityNotFoundException.class, () -> availabilityService.getAvailabilityById(1L));

        verify(availabilityRepository, times(1)).findById(1L);
    }

    @Test
    void getAllAvailabilities_ShouldReturnListOfAvailabilityDTOs() {
        // Arrange
        Availability anotherAvailability = new Availability();
        anotherAvailability.setId(2L);
        anotherAvailability.setStartTime(LocalDateTime.of(2025, 4, 21, 8, 0));
        anotherAvailability.setEndTime(LocalDateTime.of(2025, 4, 21, 16, 0));

        AvailabilityResponseDTO anotherResponseDTO = new AvailabilityResponseDTO();
        anotherResponseDTO.setId(2L);
        anotherResponseDTO.setStartTime(LocalDateTime.of(2025, 4, 21, 8, 0));
        anotherResponseDTO.setEndTime(LocalDateTime.of(2025, 4, 21, 16, 0));

        when(availabilityRepository.findAll()).thenReturn(Arrays.asList(availability, anotherAvailability));
        when(availabilityMapper.toDto(availability)).thenReturn(availabilityResponseDTO);
        when(availabilityMapper.toDto(anotherAvailability)).thenReturn(anotherResponseDTO);

        // Act
        List<AvailabilityResponseDTO> result = availabilityService.getAllAvailabilities();

        // Assert
        assertEquals(2, result.size());
        verify(availabilityRepository, times(1)).findAll();
        verify(availabilityMapper, times(1)).toDto(availability);
        verify(availabilityMapper, times(1)).toDto(anotherAvailability);
    }


    @Test
    void createAvailability_ShouldSaveAndReturnAvailabilityDTO() {
        // Arrange
        Status mockStatus = new Status();
        mockStatus.setId(1L);
        mockStatus.setName("Disponible");

        when(availabilityMapper.toEntity(availabilityDTO)).thenReturn(availability);
        when(statusRepository.findById(1L)).thenReturn(Optional.of(mockStatus));
        when(availabilityRepository.save(availability)).thenReturn(availability);
        when(availabilityMapper.toDto(availability)).thenReturn(availabilityResponseDTO);

        // Act
        AvailabilityResponseDTO result = availabilityService.createAvailability(availabilityDTO);

        // Assert
        assertNotNull(result);
        assertEquals(availabilityResponseDTO, result);
        verify(availabilityRepository, times(1)).save(availability);
        verify(availabilityMapper, times(1)).toEntity(availabilityDTO);
        verify(availabilityMapper, times(1)).toDto(availability);
    }

    @Test
    void updateAvailability_ShouldUpdateAndReturnAvailabilityDTO_WhenIdExists() {
        // Arrange
        Availability updatedAvailability = new Availability();
        updatedAvailability.setId(1L);
        updatedAvailability.setStartTime(LocalDateTime.of(2025, 4, 22, 10, 0));
        updatedAvailability.setEndTime(LocalDateTime.of(2025, 4, 22, 18, 0));

        AvailabilityResponseDTO updatedResponseDTO = new AvailabilityResponseDTO();
        updatedResponseDTO.setId(1L);
        updatedResponseDTO.setStartTime(LocalDateTime.of(2025, 4, 22, 10, 0));
        updatedResponseDTO.setEndTime(LocalDateTime.of(2025, 4, 22, 18, 0));

        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));
        when(availabilityRepository.save(any(Availability.class))).thenReturn(updatedAvailability);
        when(availabilityMapper.toDto(updatedAvailability)).thenReturn(updatedResponseDTO);

        // Act
        AvailabilityResponseDTO result = availabilityService.updateAvailability(1L, availabilityDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(availabilityRepository, times(1)).findById(1L);
        verify(availabilityRepository, times(1)).save(any(Availability.class));
    }

    @Test
    void updateAvailability_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(availabilityRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AvailabilityNotFoundException.class, () -> availabilityService.updateAvailability(99L, availabilityDTO));

        verify(availabilityRepository, times(1)).findById(99L);
        verify(availabilityRepository, never()).save(any());
    }

    @Test
    void deleteAvailability_ShouldDeleteAvailability_WhenIdExists() {
        // Arrange
        when(availabilityRepository.existsById(1L)).thenReturn(true);
        doNothing().when(availabilityRepository).deleteById(1L);

        // Act
        availabilityService.deleteAvailability(1L);

        // Assert
        verify(availabilityRepository, times(1)).existsById(1L);
        verify(availabilityRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteAvailability_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        when(availabilityRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(AvailabilityNotFoundException.class, () -> availabilityService.deleteAvailability(1L));

        verify(availabilityRepository, times(1)).existsById(1L);
        verify(availabilityRepository, never()).deleteById(any());
    }
}