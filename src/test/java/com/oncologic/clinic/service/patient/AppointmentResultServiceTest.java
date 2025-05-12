package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.AppointmentResultRequestDTO;
import com.oncologic.clinic.dto.patient.response.AppointmentResultResponseDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.exception.runtime.patient.AppointmentResultNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.MedicalHistoryNotFoundException;
import com.oncologic.clinic.mapper.patient.AppointmentResultMapper;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.patient.impl.AppointmentResultServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentResultServiceTest {

    @Mock
    private AppointmentResultRepository appointmentResultRepository;

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @Mock
    private AppointmentResultMapper appointmentResultMapper;

    @InjectMocks
    private AppointmentResultServiceImpl appointmentResultService;

    private AppointmentResult appointmentResult;
    private AppointmentResultResponseDTO responseDTO;
    private AppointmentResultRequestDTO requestDTO;
    private MedicalHistory medicalHistory;

    @BeforeEach
    void setUp() {
        medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        appointmentResult = new AppointmentResult();
        appointmentResult.setId(1L);
        appointmentResult.setEvaluationDate(LocalDateTime.now());
        appointmentResult.setMedicalHistory(medicalHistory);

        responseDTO = new AppointmentResultResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setEvaluationDate(appointmentResult.getEvaluationDate());

        requestDTO = new AppointmentResultRequestDTO();
        requestDTO.setMedicalHistoryId(1L);
    }

    @Test
    void getAppointmentResultById_WhenResultExists_ReturnsAppointmentResultDTO() {
        // Arrange
        when(appointmentResultRepository.findById(1L)).thenReturn(Optional.of(appointmentResult));
        when(appointmentResultMapper.toDto(appointmentResult)).thenReturn(responseDTO);

        // Act
        AppointmentResultResponseDTO result = appointmentResultService.getAppointmentResultById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(appointmentResultRepository, times(1)).findById(1L);
        verify(appointmentResultMapper, times(1)).toDto(appointmentResult);
    }

    @Test
    void getAppointmentResultById_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        when(appointmentResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppointmentResultNotFoundException.class, () ->
                appointmentResultService.getAppointmentResultById(99L));
        verify(appointmentResultRepository, times(1)).findById(99L);
    }

    @Test
    void getAllAppointmentResults_WhenResultsExist_ReturnsAppointmentResultDTOList() {
        // Arrange
        AppointmentResult result2 = new AppointmentResult();
        result2.setId(2L);

        AppointmentResultResponseDTO responseDTO2 = new AppointmentResultResponseDTO();
        responseDTO2.setId(2L);

        when(appointmentResultRepository.findAll()).thenReturn(List.of(appointmentResult, result2));
        when(appointmentResultMapper.toDto(appointmentResult)).thenReturn(responseDTO);
        when(appointmentResultMapper.toDto(result2)).thenReturn(responseDTO2);

        // Act
        List<AppointmentResultResponseDTO> results = appointmentResultService.getAllAppointmentResults();

        // Assert
        assertEquals(2, results.size());
        verify(appointmentResultRepository, times(1)).findAll();
        verify(appointmentResultMapper, times(1)).toDto(appointmentResult);
        verify(appointmentResultMapper, times(1)).toDto(result2);
    }

    @Test
    void createAppointmentResult_WhenValidRequest_ReturnsSavedAppointmentResultDTO() {
        // Arrange
        when(medicalHistoryRepository.findById(1L)).thenReturn(Optional.of(medicalHistory));
        when(appointmentResultMapper.toEntity(requestDTO)).thenReturn(appointmentResult);
        when(appointmentResultRepository.save(appointmentResult)).thenReturn(appointmentResult);
        when(appointmentResultMapper.toDto(appointmentResult)).thenReturn(responseDTO);

        // Act
        AppointmentResultResponseDTO result = appointmentResultService.createAppointmentResult(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicalHistoryRepository, times(1)).findById(1L);
        verify(appointmentResultRepository, times(1)).save(appointmentResult);
    }

    @Test
    void createAppointmentResult_WhenMedicalHistoryNotFound_ThrowsException() {
        // Arrange
        when(medicalHistoryRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalHistoryNotFoundException.class, () ->
                appointmentResultService.createAppointmentResult(requestDTO));
        verify(medicalHistoryRepository, times(1)).findById(1L);
        verify(appointmentResultRepository, never()).save(any());
    }

    @Test
    void updateAppointmentResult_WhenResultExists_ReturnsUpdatedAppointmentResultDTO() {
        // Arrange
        AppointmentResult updated = new AppointmentResult();
        updated.setId(1L);
        updated.setEvaluationDate(LocalDateTime.now().plusDays(1));

        AppointmentResultResponseDTO updatedResponse = new AppointmentResultResponseDTO();
        updatedResponse.setId(1L);
        updatedResponse.setEvaluationDate(updated.getEvaluationDate());

        when(appointmentResultRepository.findById(1L)).thenReturn(Optional.of(appointmentResult));
        when(medicalHistoryRepository.findById(1L)).thenReturn(Optional.of(medicalHistory));
        when(appointmentResultRepository.save(any(AppointmentResult.class))).thenReturn(updated);
        when(appointmentResultMapper.toDto(updated)).thenReturn(updatedResponse);

        // Act
        AppointmentResultResponseDTO result = appointmentResultService.updateAppointmentResult(1L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updated.getEvaluationDate(), result.getEvaluationDate());
        verify(appointmentResultRepository, times(1)).findById(1L);
        verify(appointmentResultRepository, times(1)).save(any(AppointmentResult.class));
    }

    @Test
    void deleteAppointmentResult_WhenResultExists_DeletesResult() {
        // Arrange
        when(appointmentResultRepository.findById(1L)).thenReturn(Optional.of(appointmentResult));
        doNothing().when(appointmentResultRepository).delete(appointmentResult);

        // Act
        appointmentResultService.deleteAppointmentResult(1L);

        // Assert
        verify(appointmentResultRepository, times(1)).findById(1L);
        verify(appointmentResultRepository, times(1)).delete(appointmentResult);
    }

    @Test
    void deleteAppointmentResult_WhenResultDoesNotExist_ThrowsException() {
        // Arrange
        when(appointmentResultRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppointmentResultNotFoundException.class, () ->
                appointmentResultService.deleteAppointmentResult(99L));
        verify(appointmentResultRepository, times(1)).findById(99L);
        verify(appointmentResultRepository, never()).delete(any());
    }
}