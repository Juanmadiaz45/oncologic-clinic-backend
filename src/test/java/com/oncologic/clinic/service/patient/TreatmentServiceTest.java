/*package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.service.patient.impl.TreatmentServiceImpl;
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
public class TreatmentServiceTest {

    @Mock
    private TreatmentRepository treatmentRepository;

    @InjectMocks
    private TreatmentServiceImpl treatmentService;

    @Test
    void getTreatmentById_WhenIdExists_ReturnsTreatment() {
        // Arrange
        Long id = 1L;
        Treatment expectedTreatment = new Treatment();
        expectedTreatment.setId(id);
        expectedTreatment.setName("Chemotherapy");

        when(treatmentRepository.findById(id)).thenReturn(Optional.of(expectedTreatment));

        // Act
        Treatment result = treatmentService.getTreatmentById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Chemotherapy", result.getName());

        verify(treatmentRepository, times(1)).findById(id);
    }

    @Test
    void getTreatmentById_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(treatmentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> treatmentService.getTreatmentById(id));

        verify(treatmentRepository, times(1)).findById(id);
    }

    @Test
    void getAllTreatments_WhenCalled_ReturnsTreatmentList() {
        // Arrange
        Treatment treatment1 = new Treatment();
        treatment1.setId(1L);
        treatment1.setName("Treatment 1");

        Treatment treatment2 = new Treatment();
        treatment2.setId(2L);
        treatment2.setName("Treatment 2");

        List<Treatment> expectedList = Arrays.asList(treatment1, treatment2);

        when(treatmentRepository.findAll()).thenReturn(expectedList);

        // Act
        List<Treatment> result = treatmentService.getAllTreatments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Treatment 1", result.get(0).getName());
        assertEquals("Treatment 2", result.get(1).getName());

        verify(treatmentRepository, times(1)).findAll();
    }

    @Test
    void createTreatment_WhenValidTreatment_ReturnsSavedTreatment() {
        // Arrange
        Treatment treatmentToSave = new Treatment();
        treatmentToSave.setName("Radiotherapy");
        treatmentToSave.setDescription("Radiation treatment");

        Treatment savedTreatment = new Treatment();
        savedTreatment.setId(1L);
        savedTreatment.setName("Radiotherapy");
        savedTreatment.setDescription("Radiation treatment");

        when(treatmentRepository.save(treatmentToSave)).thenReturn(savedTreatment);

        // Act
        Treatment result = treatmentService.createTreatment(treatmentToSave);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Radiotherapy", result.getName());
        assertEquals("Radiation treatment", result.getDescription());

        verify(treatmentRepository, times(1)).save(treatmentToSave);
    }

    @Test
    void updateTreatment_WhenTreatmentExists_ReturnsUpdatedTreatment() {
        // Arrange
        Long id = 1L;
        Treatment existingTreatment = new Treatment();
        existingTreatment.setId(id);
        existingTreatment.setName("Old Treatment");

        Treatment updatedTreatment = new Treatment();
        updatedTreatment.setId(id);
        updatedTreatment.setName("Updated Treatment");

        when(treatmentRepository.findById(id)).thenReturn(Optional.of(existingTreatment));
        when(treatmentRepository.save(existingTreatment)).thenReturn(updatedTreatment);

        // Act
        Treatment result = treatmentService.updateTreatment(updatedTreatment);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Treatment", result.getName());

        verify(treatmentRepository, times(1)).findById(id);
        verify(treatmentRepository, times(1)).save(existingTreatment);
    }

    @Test
    void deleteTreatment_WhenIdExists_DeletesTreatment() {
        // Arrange
        Long id = 1L;
        Treatment treatment = new Treatment();
        treatment.setId(id);

        when(treatmentRepository.findById(id)).thenReturn(Optional.of(treatment));

        // Act
        treatmentService.deleteTreatment(id);

        // Assert
        verify(treatmentRepository, times(1)).findById(id);
        verify(treatmentRepository, times(1)).delete(treatment);
    }
}*/