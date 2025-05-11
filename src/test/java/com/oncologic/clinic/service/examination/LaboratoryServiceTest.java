/*package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.entity.examination.Laboratory;
import com.oncologic.clinic.repository.examination.LaboratoryRepository;
import com.oncologic.clinic.service.examination.impl.LaboratoryServiceImpl;
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
public class LaboratoryServiceTest {

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @InjectMocks
    private LaboratoryServiceImpl laboratoryService;

    @Test
    void getLaboratoryById_WhenLabExists_ReturnsLaboratory() {
        // Arrange
        Long id = 1L;
        Laboratory mockLab = new Laboratory();
        mockLab.setId(id);
        when(laboratoryRepository.findById(id)).thenReturn(Optional.of(mockLab));

        // Act
        Laboratory result = laboratoryService.getLaboratoryById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(laboratoryRepository, times(1)).findById(id);
    }

    @Test
    void getLaboratoryById_WhenLabDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(laboratoryRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> laboratoryService.getLaboratoryById(id));
        verify(laboratoryRepository, times(1)).findById(id);
    }

    @Test
    void getAllLaboratories_WhenLabsExist_ReturnsLaboratoryList() {
        // Arrange
        Laboratory lab1 = new Laboratory();
        lab1.setId(1L);
        Laboratory lab2 = new Laboratory();
        lab2.setId(2L);
        List<Laboratory> mockLabs = Arrays.asList(lab1, lab2);
        when(laboratoryRepository.findAll()).thenReturn(mockLabs);

        // Act
        List<Laboratory> results = laboratoryService.getAllLaboratories();

        // Assert
        assertEquals(2, results.size());
        verify(laboratoryRepository, times(1)).findAll();
    }

    @Test
    void getAllLaboratories_WhenNoLabsExist_ReturnsEmptyList() {
        // Arrange
        when(laboratoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<Laboratory> results = laboratoryService.getAllLaboratories();

        // Assert
        assertTrue(results.isEmpty());
        verify(laboratoryRepository, times(1)).findAll();
    }

    @Test
    void createLaboratory_WhenValidLab_ReturnsSavedLab() {
        // Arrange
        Laboratory newLab = new Laboratory();
        newLab.setName("Lab Corp");

        Laboratory savedLab = new Laboratory();
        savedLab.setId(1L);
        savedLab.setName(newLab.getName());

        when(laboratoryRepository.save(newLab)).thenReturn(savedLab);

        // Act
        Laboratory result = laboratoryService.createLaboratory(newLab);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Lab Corp", result.getName());
        verify(laboratoryRepository, times(1)).save(newLab);
    }

    @Test
    void createLaboratory_WhenNullLabProvided_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> laboratoryService.createLaboratory(null));
        verify(laboratoryRepository, never()).save(any());
    }

    @Test
    void updateLaboratory_WhenLabExists_ReturnsUpdatedLab() {
        // Arrange
        Long id = 1L;
        Laboratory existingLab = new Laboratory();
        existingLab.setId(id);
        existingLab.setName("Old Name");

        Laboratory updatedLab = new Laboratory();
        updatedLab.setId(id);
        updatedLab.setName("New Name");

        when(laboratoryRepository.findById(id)).thenReturn(Optional.of(existingLab));
        when(laboratoryRepository.save(existingLab)).thenReturn(updatedLab);

        // Act
        Laboratory result = laboratoryService.updateLaboratory(updatedLab);

        // Assert
        assertEquals("New Name", result.getName());
        verify(laboratoryRepository, times(1)).findById(id);
        verify(laboratoryRepository, times(1)).save(existingLab);
    }

    @Test
    void updateLaboratory_WhenLabDoesNotExist_ThrowsException() {
        // Arrange
        Laboratory nonExistentLab = new Laboratory();
        nonExistentLab.setId(99L);
        when(laboratoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> laboratoryService.updateLaboratory(nonExistentLab));
        verify(laboratoryRepository, times(1)).findById(99L);
        verify(laboratoryRepository, never()).save(any());
    }

    @Test
    void deleteLaboratory_WhenLabExists_DeletesLab() {
        // Arrange
        Long id = 1L;
        Laboratory lab = new Laboratory();
        lab.setId(id);
        when(laboratoryRepository.findById(id)).thenReturn(Optional.of(lab));

        // Act
        laboratoryService.deleteLaboratory(id);

        // Assert
        verify(laboratoryRepository, times(1)).findById(id);
        verify(laboratoryRepository, times(1)).delete(lab);
    }

    @Test
    void deleteLaboratory_WhenLabDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(laboratoryRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> laboratoryService.deleteLaboratory(id));
        verify(laboratoryRepository, times(1)).findById(id);
        verify(laboratoryRepository, never()).delete(any());
    }
}*/