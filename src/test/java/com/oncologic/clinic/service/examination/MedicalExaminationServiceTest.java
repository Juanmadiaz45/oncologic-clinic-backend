/*package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.entity.examination.MedicalExamination;
import com.oncologic.clinic.repository.examination.MedicalExaminationRepository;
import com.oncologic.clinic.service.examination.impl.MedicalExaminationServiceImpl;
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
public class MedicalExaminationServiceTest {

    @Mock
    private MedicalExaminationRepository medicalExaminationRepository;

    @InjectMocks
    private MedicalExaminationServiceImpl medicalExaminationService;

    @Test
    void getMedicalExaminationById_WhenExamExists_ReturnsMedicalExamination() {
        // Arrange
        String id = "EXAM-123";
        MedicalExamination mockExam = new MedicalExamination();
        mockExam.setId(id);
        when(medicalExaminationRepository.findById(id)).thenReturn(Optional.of(mockExam));

        // Act
        MedicalExamination result = medicalExaminationService.getMedicalExaminationById(Long.valueOf(id));

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(medicalExaminationRepository, times(1)).findById(id);
    }

    @Test
    void getMedicalExaminationById_WhenExamDoesNotExist_ThrowsException() {
        // Arrange
        String id = "NON-EXIST";
        when(medicalExaminationRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> medicalExaminationService.getMedicalExaminationById(Long.valueOf(id)));
        verify(medicalExaminationRepository, times(1)).findById(id);
    }

    @Test
    void getAllMedicalExaminations_WhenExamsExist_ReturnsMedicalExaminationList() {
        // Arrange
        MedicalExamination exam1 = new MedicalExamination();
        exam1.setId("EXAM-1");
        MedicalExamination exam2 = new MedicalExamination();
        exam2.setId("EXAM-2");
        List<MedicalExamination> mockExams = Arrays.asList(exam1, exam2);
        when(medicalExaminationRepository.findAll()).thenReturn(mockExams);

        // Act
        List<MedicalExamination> results = medicalExaminationService.getAllMedicalExaminations();

        // Assert
        assertEquals(2, results.size());
        verify(medicalExaminationRepository, times(1)).findAll();
    }

    @Test
    void getAllMedicalExaminations_WhenNoExamsExist_ReturnsEmptyList() {
        // Arrange
        when(medicalExaminationRepository.findAll()).thenReturn(List.of());

        // Act
        List<MedicalExamination> results = medicalExaminationService.getAllMedicalExaminations();

        // Assert
        assertTrue(results.isEmpty());
        verify(medicalExaminationRepository, times(1)).findAll();
    }

    @Test
    void createMedicalExamination_WhenValidExam_ReturnsSavedExam() {
        // Arrange
        MedicalExamination newExam = new MedicalExamination();
        newExam.setId("NEW-EXAM");
        newExam.setDateOfRealization(LocalDateTime.now());

        when(medicalExaminationRepository.save(newExam)).thenReturn(newExam);

        // Act
        MedicalExamination result = medicalExaminationService.createMedicalExamination(newExam);

        // Assert
        assertNotNull(result.getId());
        verify(medicalExaminationRepository, times(1)).save(newExam);
    }

    @Test
    void createMedicalExamination_WhenNullExamProvided_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> medicalExaminationService.createMedicalExamination(null));
        verify(medicalExaminationRepository, never()).save(any());
    }

    @Test
    void updateMedicalExamination_WhenExamExists_ReturnsUpdatedExam() {
        // Arrange
        String id = "EXAM-123";
        MedicalExamination existingExam = new MedicalExamination();
        existingExam.setId(id);
        existingExam.setDateOfRealization(LocalDateTime.now().minusDays(1));

        MedicalExamination updatedExam = new MedicalExamination();
        updatedExam.setId(id);
        updatedExam.setDateOfRealization(LocalDateTime.now());

        when(medicalExaminationRepository.findById(id)).thenReturn(Optional.of(existingExam));
        when(medicalExaminationRepository.save(existingExam)).thenReturn(updatedExam);

        // Act
        MedicalExamination result = medicalExaminationService.updateMedicalExamination(updatedExam);

        // Assert
        assertEquals(updatedExam.getDateOfRealization(), result.getDateOfRealization());
        verify(medicalExaminationRepository, times(1)).findById(id);
        verify(medicalExaminationRepository, times(1)).save(existingExam);
    }

    @Test
    void updateMedicalExamination_WhenExamDoesNotExist_ThrowsException() {
        // Arrange
        String id = "NON-EXIST";
        MedicalExamination nonExistentExam = new MedicalExamination();
        nonExistentExam.setId(id);
        when(medicalExaminationRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> medicalExaminationService.updateMedicalExamination(nonExistentExam));
        verify(medicalExaminationRepository, times(1)).findById(id);
        verify(medicalExaminationRepository, never()).save(any());
    }

    @Test
    void deleteMedicalExamination_WhenExamExists_DeletesExam() {
        // Arrange
        String id = "EXAM-123";
        MedicalExamination exam = new MedicalExamination();
        exam.setId(id);
        when(medicalExaminationRepository.findById(id)).thenReturn(Optional.of(exam));

        // Act
        medicalExaminationService.deleteMedicalExamination(Long.valueOf(id));

        // Assert
        verify(medicalExaminationRepository, times(1)).findById(id);
        verify(medicalExaminationRepository, times(1)).delete(exam);
    }

    @Test
    void deleteMedicalExamination_WhenExamDoesNotExist_ThrowsException() {
        // Arrange
        String id = "NON-EXIST";
        when(medicalExaminationRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> medicalExaminationService.deleteMedicalExamination(Long.valueOf(id)));
        verify(medicalExaminationRepository, times(1)).findById(id);
        verify(medicalExaminationRepository, never()).delete(any());
    }
}*/