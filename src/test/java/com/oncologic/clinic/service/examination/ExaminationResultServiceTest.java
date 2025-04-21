package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.entity.examination.ExaminationResult;
import com.oncologic.clinic.repository.examination.ExaminationResultRepository;
import com.oncologic.clinic.service.examination.impl.ExaminationResultServiceImpl;
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
public class ExaminationResultServiceTest {

    @Mock
    private ExaminationResultRepository examinationResultRepository;

    @InjectMocks
    private ExaminationResultServiceImpl examinationResultService;

    @Test
    void getExaminationResultById_WhenResultExists_ReturnsExaminationResult() {
        // Arrange
        Long id = 1L;
        ExaminationResult mockResult = new ExaminationResult();
        mockResult.setId(id);
        when(examinationResultRepository.findById(id)).thenReturn(Optional.of(mockResult));

        // Act
        ExaminationResult result = examinationResultService.getExaminationResultById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(examinationResultRepository, times(1)).findById(id);
    }

    @Test
    void getExaminationResultById_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(examinationResultRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> examinationResultService.getExaminationResultById(id));
        verify(examinationResultRepository, times(1)).findById(id);
    }

    @Test
    void getAllExaminationResults_WhenResultsExist_ReturnsExaminationResultList() {
        // Arrange
        ExaminationResult result1 = new ExaminationResult();
        ExaminationResult result2 = new ExaminationResult();
        List<ExaminationResult> mockResults = Arrays.asList(result1, result2);
        when(examinationResultRepository.findAll()).thenReturn(mockResults);

        // Act
        List<ExaminationResult> results = examinationResultService.getAllExaminationResults();

        // Assert
        assertEquals(2, results.size());
        verify(examinationResultRepository, times(1)).findAll();
    }

    @Test
    void getAllExaminationResults_WhenNoResultsExist_ReturnsEmptyList() {
        // Arrange
        when(examinationResultRepository.findAll()).thenReturn(List.of());

        // Act
        List<ExaminationResult> results = examinationResultService.getAllExaminationResults();

        // Assert
        assertTrue(results.isEmpty());
        verify(examinationResultRepository, times(1)).findAll();
    }

    @Test
    void createExaminationResult_WhenValidResult_ReturnsSavedResult() {
        // Arrange
        ExaminationResult newResult = new ExaminationResult();
        newResult.setGenerationDate(LocalDateTime.now());

        ExaminationResult savedResult = new ExaminationResult();
        savedResult.setId(1L);
        savedResult.setGenerationDate(newResult.getGenerationDate());

        when(examinationResultRepository.save(newResult)).thenReturn(savedResult);

        // Act
        ExaminationResult result = examinationResultService.createExaminationResult(newResult);

        // Assert
        assertNotNull(result.getId());
        verify(examinationResultRepository, times(1)).save(newResult);
    }

    @Test
    void createExaminationResult_WhenNullResultProvided_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> examinationResultService.createExaminationResult(null));
        verify(examinationResultRepository, never()).save(any());
    }

    @Test
    void updateExaminationResult_WhenResultExists_ReturnsUpdatedResult() {
        // Arrange
        Long id = 1L;
        ExaminationResult existingResult = new ExaminationResult();
        existingResult.setId(id);

        ExaminationResult updatedResult = new ExaminationResult();
        updatedResult.setId(id);
        updatedResult.setGenerationDate(LocalDateTime.now().plusDays(1));

        when(examinationResultRepository.findById(id)).thenReturn(Optional.of(existingResult));
        when(examinationResultRepository.save(existingResult)).thenReturn(updatedResult);

        // Act
        ExaminationResult result = examinationResultService.updateExaminationResult(updatedResult);

        // Assert
        assertEquals(updatedResult.getGenerationDate(), result.getGenerationDate());
        verify(examinationResultRepository, times(1)).findById(id);
        verify(examinationResultRepository, times(1)).save(existingResult);
    }

    @Test
    void updateExaminationResult_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        ExaminationResult nonExistentResult = new ExaminationResult();
        nonExistentResult.setId(99L);
        when(examinationResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> examinationResultService.updateExaminationResult(nonExistentResult));
        verify(examinationResultRepository, times(1)).findById(99L);
        verify(examinationResultRepository, never()).save(any());
    }

    @Test
    void deleteExaminationResult_WhenResultExists_DeletesResult() {
        // Arrange
        Long id = 1L;
        ExaminationResult result = new ExaminationResult();
        result.setId(id);
        when(examinationResultRepository.findById(id)).thenReturn(Optional.of(result));

        // Act
        examinationResultService.deleteExaminationResult(id);

        // Assert
        verify(examinationResultRepository, times(1)).findById(id);
        verify(examinationResultRepository, times(1)).delete(result);
    }

    @Test
    void deleteExaminationResult_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(examinationResultRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> examinationResultService.deleteExaminationResult(id));
        verify(examinationResultRepository, times(1)).findById(id);
        verify(examinationResultRepository, never()).delete(any());
    }
}