package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.request.ExaminationResultRequestDTO;
import com.oncologic.clinic.dto.examination.response.ExaminationResultResponseDTO;
import com.oncologic.clinic.dto.examination.update.ExaminationResultUpdateDTO;
import com.oncologic.clinic.entity.examination.ExaminationResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.exception.runtime.examination.ExaminationResultNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.MedicalHistoryNotFoundException;
import com.oncologic.clinic.mapper.examination.ExaminationResultMapper;
import com.oncologic.clinic.repository.examination.ExaminationResultRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.examination.impl.ExaminationResultServiceImpl;
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
public class ExaminationResultServiceTest {

    @Mock
    private ExaminationResultRepository examinationResultRepository;

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @Mock
    private ExaminationResultMapper examinationResultMapper;

    @InjectMocks
    private ExaminationResultServiceImpl examinationResultService;

    private ExaminationResult examinationResult;
    private ExaminationResultResponseDTO responseDTO;
    private ExaminationResultRequestDTO requestDTO;
    private ExaminationResultUpdateDTO updateDTO;
    private MedicalHistory medicalHistory;

    @BeforeEach
    void setUp() {
        examinationResult = new ExaminationResult();
        examinationResult.setId(1L);
        examinationResult.setGenerationDate(LocalDateTime.now());

        medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);
        examinationResult.setMedicalHistory(medicalHistory);

        responseDTO = new ExaminationResultResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setGenerationDate(examinationResult.getGenerationDate());

        requestDTO = new ExaminationResultRequestDTO();
        requestDTO.setMedicalHistoryId(1L);

        updateDTO = new ExaminationResultUpdateDTO();
        updateDTO.setGenerationDate(LocalDateTime.now().plusDays(1));
    }

    @Test
    void getExaminationResultById_WhenResultExists_ReturnsExaminationResultDTO() {
        // Arrange
        when(examinationResultRepository.findById(1L)).thenReturn(Optional.of(examinationResult));
        when(examinationResultMapper.toDto(examinationResult)).thenReturn(responseDTO);

        // Act
        ExaminationResultResponseDTO result = examinationResultService.getExaminationResultById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(examinationResultRepository, times(1)).findById(1L);
        verify(examinationResultMapper, times(1)).toDto(examinationResult);
    }

    @Test
    void getExaminationResultById_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        when(examinationResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExaminationResultNotFoundException.class, () ->
                examinationResultService.getExaminationResultById(99L));
        verify(examinationResultRepository, times(1)).findById(99L);
    }

    @Test
    void getAllExaminationResults_WhenResultsExist_ReturnsExaminationResultDTOList() {
        // Arrange
        ExaminationResult result2 = new ExaminationResult();
        result2.setId(2L);

        ExaminationResultResponseDTO responseDTO2 = new ExaminationResultResponseDTO();
        responseDTO2.setId(2L);

        when(examinationResultRepository.findAll()).thenReturn(Arrays.asList(examinationResult, result2));
        when(examinationResultMapper.toDto(examinationResult)).thenReturn(responseDTO);
        when(examinationResultMapper.toDto(result2)).thenReturn(responseDTO2);

        // Act
        List<ExaminationResultResponseDTO> results = examinationResultService.getAllExaminationResults();

        // Assert
        assertEquals(2, results.size());
        verify(examinationResultRepository, times(1)).findAll();
        verify(examinationResultMapper, times(1)).toDto(examinationResult);
        verify(examinationResultMapper, times(1)).toDto(result2);
    }

    @Test
    void createExaminationResult_WhenValidRequest_ReturnsSavedResultDTO() {
        // Arrange
        when(medicalHistoryRepository.findById(1L)).thenReturn(Optional.of(medicalHistory));
        when(examinationResultMapper.toEntity(requestDTO)).thenReturn(examinationResult);
        when(examinationResultRepository.save(examinationResult)).thenReturn(examinationResult);
        when(examinationResultMapper.toDto(examinationResult)).thenReturn(responseDTO);

        // Act
        ExaminationResultResponseDTO result = examinationResultService.createExaminationResult(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicalHistoryRepository, times(1)).findById(1L);
        verify(examinationResultRepository, times(1)).save(examinationResult);
    }

    @Test
    void createExaminationResult_WhenMedicalHistoryNotFound_ThrowsException() {
        // Arrange
        when(medicalHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalHistoryNotFoundException.class, () ->
                examinationResultService.createExaminationResult(requestDTO));
        verify(medicalHistoryRepository, times(1)).findById(1L);
        verify(examinationResultRepository, never()).save(any());
    }

    @Test
    void updateExaminationResult_WhenResultExists_ReturnsUpdatedResultDTO() {
        // Arrange
        ExaminationResult updated = new ExaminationResult();
        updated.setId(1L);
        updated.setGenerationDate(updateDTO.getGenerationDate());

        ExaminationResultResponseDTO updatedResponse = new ExaminationResultResponseDTO();
        updatedResponse.setId(1L);
        updatedResponse.setGenerationDate(updateDTO.getGenerationDate());

        when(examinationResultRepository.findById(1L)).thenReturn(Optional.of(examinationResult));
        when(examinationResultRepository.save(any(ExaminationResult.class))).thenReturn(updated);
        when(examinationResultMapper.toDto(updated)).thenReturn(updatedResponse);

        // Act
        ExaminationResultResponseDTO result = examinationResultService.updateExaminationResult(1L, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updateDTO.getGenerationDate(), result.getGenerationDate());
        verify(examinationResultRepository, times(1)).findById(1L);
        verify(examinationResultRepository, times(1)).save(any(ExaminationResult.class));
    }

    @Test
    void updateExaminationResult_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        when(examinationResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExaminationResultNotFoundException.class, () ->
                examinationResultService.updateExaminationResult(99L, updateDTO));
        verify(examinationResultRepository, times(1)).findById(99L);
        verify(examinationResultRepository, never()).save(any());
    }

    @Test
    void deleteExaminationResult_WhenResultExists_DeletesResult() {
        // Arrange
        when(examinationResultRepository.existsById(1L)).thenReturn(true);
        doNothing().when(examinationResultRepository).deleteById(1L);

        // Act
        examinationResultService.deleteExaminationResult(1L);

        // Assert
        verify(examinationResultRepository, times(1)).existsById(1L);
        verify(examinationResultRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteExaminationResult_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        when(examinationResultRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ExaminationResultNotFoundException.class, () ->
                examinationResultService.deleteExaminationResult(99L));
        verify(examinationResultRepository, times(1)).existsById(99L);
        verify(examinationResultRepository, never()).deleteById(any());
    }
}