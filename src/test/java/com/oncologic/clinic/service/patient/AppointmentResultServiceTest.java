/*package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.service.patient.impl.AppointmentResultServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentResultServiceTest {
    @Mock
    private AppointmentResultRepository appointmentResultRepository;

    @InjectMocks
    private AppointmentResultServiceImpl appointmentResultService;

    @Test
    void getAppointmentResultById_WhenResultExists_ReturnsAppointmentResult() {
        // Arrange
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        AppointmentResult result = new AppointmentResult();
        result.setId(1L);
        result.setEvaluationDate(LocalDateTime.now());
        result.setMedicalHistory(medicalHistory);

        when(appointmentResultRepository.findById(1L)).thenReturn(Optional.of(result));

        // Act
        AppointmentResult foundResult = appointmentResultService.getAppointmentResultById(1L);

        // Assert
        assertNotNull(foundResult);
        assertEquals(1L, foundResult.getId());
        assertEquals(medicalHistory, foundResult.getMedicalHistory());

        verify(appointmentResultRepository, times(1)).findById(1L);
    }

    @Test
    void getAppointmentResultById_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        when(appointmentResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                appointmentResultService.getAppointmentResultById(99L));

        assertEquals("Appointment result not found", exception.getMessage());

        verify(appointmentResultRepository, times(1)).findById(99L);
    }

    @Test
    void getAllAppointmentResults_WhenResultsExist_ReturnsResultsList() {
        // Arrange
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        AppointmentResult result1 = new AppointmentResult();
        result1.setId(1L);
        result1.setEvaluationDate(LocalDateTime.now());
        result1.setMedicalHistory(medicalHistory);

        AppointmentResult result2 = new AppointmentResult();
        result2.setId(2L);
        result2.setEvaluationDate(LocalDateTime.now().plusDays(1));
        result2.setMedicalHistory(medicalHistory);

        List<AppointmentResult> mockResults = Arrays.asList(result1, result2);

        when(appointmentResultRepository.findAll()).thenReturn(mockResults);

        // Act
        List<AppointmentResult> results = appointmentResultService.getAllAppointmentResults();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(1L, results.get(0).getId());
        assertEquals(2L, results.get(1).getId());

        verify(appointmentResultRepository, times(1)).findAll();
    }

    @Test
    void getAllAppointmentResults_WhenNoResultsExist_ReturnsEmptyList() {
        // Arrange
        when(appointmentResultRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<AppointmentResult> results = appointmentResultService.getAllAppointmentResults();

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());

        verify(appointmentResultRepository, times(1)).findAll();
    }

    @Test
    void createAppointmentResult_WhenValidResult_ReturnsSavedResult() {
        // Arrange
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        AppointmentResult result = new AppointmentResult();
        result.setEvaluationDate(LocalDateTime.now());
        result.setMedicalHistory(medicalHistory);

        AppointmentResult savedResult = new AppointmentResult();
        savedResult.setId(1L);
        savedResult.setEvaluationDate(result.getEvaluationDate());
        savedResult.setMedicalHistory(medicalHistory);

        when(appointmentResultRepository.save(result)).thenReturn(savedResult);

        // Act
        AppointmentResult createdResult = appointmentResultService.createAppointmentResult(result);

        // Assert
        assertNotNull(createdResult);
        assertEquals(1L, createdResult.getId());
        assertEquals(result.getEvaluationDate(), createdResult.getEvaluationDate());
        assertEquals(medicalHistory, createdResult.getMedicalHistory());

        verify(appointmentResultRepository, times(1)).save(result);
    }

    @Test
    void createAppointmentResult_WhenNullResultProvided_ThrowsIllegalArgumentException() {

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                appointmentResultService.createAppointmentResult(null));

        verify(appointmentResultRepository, never()).save(any());
    }

    @Test
    void updateAppointmentResult_WhenResultExists_ReturnsUpdatedResult() {
        // Arrange
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        LocalDateTime oldDate = LocalDateTime.now();
        LocalDateTime newDate = LocalDateTime.now().plusDays(1);

        AppointmentResult existingResult = new AppointmentResult();
        existingResult.setId(1L);
        existingResult.setEvaluationDate(oldDate);
        existingResult.setMedicalHistory(medicalHistory);

        AppointmentResult updatedResult = new AppointmentResult();
        updatedResult.setId(1L);
        updatedResult.setEvaluationDate(newDate);
        updatedResult.setMedicalHistory(medicalHistory);

        when(appointmentResultRepository.findById(1L)).thenReturn(Optional.of(existingResult));
        when(appointmentResultRepository.save(any(AppointmentResult.class))).thenReturn(updatedResult);

        // Act
        AppointmentResult result = appointmentResultService.updateAppointmentResult(updatedResult);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(newDate, result.getEvaluationDate());

        verify(appointmentResultRepository, times(1)).findById(1L);
        verify(appointmentResultRepository, times(1)).save(existingResult);
    }

    @Test
    void updateAppointmentResult_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        AppointmentResult nonExistentResult = new AppointmentResult();
        nonExistentResult.setId(99L);
        nonExistentResult.setEvaluationDate(LocalDateTime.now());
        nonExistentResult.setMedicalHistory(medicalHistory);

        when(appointmentResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                appointmentResultService.updateAppointmentResult(nonExistentResult));

        assertEquals("Appointment result not found", exception.getMessage());

        verify(appointmentResultRepository, times(1)).findById(99L);
        verify(appointmentResultRepository, never()).save(any(AppointmentResult.class));
    }

    @Test
    void deleteAppointmentResult_WhenResultExists_DeletesResult() {
        // Arrange
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        AppointmentResult result = new AppointmentResult();
        result.setId(1L);
        result.setEvaluationDate(LocalDateTime.now());
        result.setMedicalHistory(medicalHistory);

        when(appointmentResultRepository.findById(1L)).thenReturn(Optional.of(result));

        // Act
        appointmentResultService.deleteAppointmentResult(1L);

        // Assert
        verify(appointmentResultRepository, times(1)).findById(1L);
        verify(appointmentResultRepository, times(1)).delete(result);
    }

    @Test
    void deleteAppointmentResult_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        when(appointmentResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                appointmentResultService.deleteAppointmentResult(99L));

        assertEquals("Appointment result not found", exception.getMessage());

        verify(appointmentResultRepository, times(1)).findById(99L);
        verify(appointmentResultRepository, never()).delete(any(AppointmentResult.class));
    }
}*/