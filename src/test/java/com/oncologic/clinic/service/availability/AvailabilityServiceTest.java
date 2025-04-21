package com.oncologic.clinic.service.availability;

import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.repository.availability.AvailabilityRepository;
import com.oncologic.clinic.service.availability.impl.AvailabilityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailabilityServiceTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private Availability availability;

    @BeforeEach
    void setUp() {
        availability = new Availability();
        availability.setId(1L);
        availability.setStartTime(LocalDateTime.of(2025, 4, 20, 9, 0));
        availability.setEndTime(LocalDateTime.of(2025, 4, 20, 17, 0));
    }

    @Test
    void getAvailabilityById_ShouldReturnAvailability_WhenIdExists() {
        // Arrange
        when(availabilityRepository.findById(1L)).thenReturn(Optional.of(availability));

        // Act
        Availability result = availabilityService.getAvailabilityById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(availabilityRepository, times(1)).findById(1L);
    }

    @Test
    void getAvailabilityById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        when(availabilityRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Availability result = availabilityService.getAvailabilityById(1L);

        // Assert
        assertNull(result);
        verify(availabilityRepository, times(1)).findById(1L);
    }

    @Test
    void getAllAvailabilities_ShouldReturnListOfAvailabilities() {
        // Arrange
        Availability anotherAvailability = new Availability();
        anotherAvailability.setId(2L);
        anotherAvailability.setStartTime(LocalDateTime.of(2025, 4, 21, 8, 0));
        anotherAvailability.setEndTime(LocalDateTime.of(2025, 4, 21, 16, 0));

        when(availabilityRepository.findAll()).thenReturn(Arrays.asList(availability, anotherAvailability));

        // Act
        List<Availability> result = availabilityService.getAllAvailabilities();

        // Assert
        assertEquals(2, result.size());
        verify(availabilityRepository, times(1)).findAll();
    }

    @Test
    void createAvailability_ShouldSaveAndReturnAvailability() {
        // Arrange
        when(availabilityRepository.save(availability)).thenReturn(availability);

        // Act
        Availability result = availabilityService.createAvailability(availability);

        // Assert
        assertNotNull(result);
        assertEquals(availability, result);
        verify(availabilityRepository, times(1)).save(availability);
    }

    @Test
    void updateAvailability_ShouldUpdateAndReturnAvailability_WhenIdExists() {
        // Arrange
        Availability updated = new Availability();
        updated.setId(1L);
        updated.setStartTime(LocalDateTime.of(2025, 4, 22, 10, 0));
        updated.setEndTime(LocalDateTime.of(2025, 4, 22, 18, 0));

        when(availabilityRepository.existsById(1L)).thenReturn(true);
        when(availabilityRepository.save(updated)).thenReturn(updated);

        // Act
        Availability result = availabilityService.updateAvailability(updated);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(availabilityRepository, times(1)).existsById(1L);
        verify(availabilityRepository, times(1)).save(updated);
    }

    @Test
    void updateAvailability_ShouldReturnNull_WhenIdDoesNotExist() {
        // Arrange
        Availability updated = new Availability();
        updated.setId(99L);

        when(availabilityRepository.existsById(99L)).thenReturn(false);

        // Act
        Availability result = availabilityService.updateAvailability(updated);

        // Assert
        assertNull(result);
        verify(availabilityRepository, times(1)).existsById(99L);
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
    void deleteAvailability_ShouldNotDelete_WhenIdDoesNotExist() {
        // Arrange
        when(availabilityRepository.existsById(1L)).thenReturn(false);

        // Act
        availabilityService.deleteAvailability(1L);

        // Assert
        verify(availabilityRepository, times(1)).existsById(1L);
        verify(availabilityRepository, never()).deleteById(any());
    }
}
