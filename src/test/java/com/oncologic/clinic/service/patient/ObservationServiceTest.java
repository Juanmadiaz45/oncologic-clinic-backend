package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.ObservationRequestDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.update.ObservationUpdateDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Observation;
import com.oncologic.clinic.exception.runtime.patient.AppointmentResultNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.ObservationNotFoundException;
import com.oncologic.clinic.mapper.patient.ObservationMapper;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.ObservationRepository;
import com.oncologic.clinic.service.patient.impl.ObservationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ObservationServiceTest {
    @Mock
    private ObservationRepository observationRepository;

    @Mock
    private AppointmentResultRepository appointmentResultRepository;

    @Mock
    private ObservationMapper observationMapper;

    @InjectMocks
    private ObservationServiceImpl observationService;

    private AppointmentResult createTestAppointmentResult(Long id) {
        AppointmentResult result = new AppointmentResult();
        result.setId(id);
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);
        result.setMedicalHistory(medicalHistory);
        return result;
    }

    private Observation createTestObservation(Long id, String content, String recommendation, AppointmentResult result) {
        Observation observation = new Observation();
        observation.setId(id);
        observation.setContent(content);
        observation.setRecommendation(recommendation);
        observation.setAppointmentResult(result);
        return observation;
    }

    private ObservationResponseDTO createTestObservationDTO(Long id, String content, String recommendation) {
        return ObservationResponseDTO.builder()
                .id(id)
                .content(content)
                .recommendation(recommendation)
                .build();
    }

    @Test
    void getObservationById_WhenObservationExists_ReturnsObservationDTO() {
        // Arrange
        Long id = 1L;
        AppointmentResult result = createTestAppointmentResult(1L);
        Observation observation = createTestObservation(id, "Paciente mejora notable", "Continuar tratamiento", result);
        ObservationResponseDTO responseDTO = createTestObservationDTO(id, "Paciente mejora notable", "Continuar tratamiento");

        when(observationRepository.findById(id)).thenReturn(Optional.of(observation));
        when(observationMapper.toDto(observation)).thenReturn(responseDTO);

        // Act
        ObservationResponseDTO foundObservation = observationService.getObservationById(id);

        // Assert
        assertNotNull(foundObservation);
        assertEquals(id, foundObservation.getId());
        assertEquals("Paciente mejora notable", foundObservation.getContent());
        assertEquals("Continuar tratamiento", foundObservation.getRecommendation());

        verify(observationRepository).findById(id);
        verify(observationMapper).toDto(observation);
    }

    @Test
    void getObservationById_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 99L;
        when(observationRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ObservationNotFoundException.class,
                () -> observationService.getObservationById(nonExistentId));

        assertEquals("Observation not found with ID: " + nonExistentId, exception.getMessage());
        verify(observationRepository).findById(nonExistentId);
        verify(observationMapper, never()).toDto(any());
    }

    @Test
    void getAllObservations_WhenObservationsExist_ReturnsObservationsDTOList() {
        // Arrange
        AppointmentResult result = createTestAppointmentResult(1L);
        Observation observation1 = createTestObservation(1L, "Primera observación", "Recomendación 1", result);
        Observation observation2 = createTestObservation(2L, "Segunda observación", "Recomendación 2", result);

        ObservationResponseDTO dto1 = createTestObservationDTO(1L, "Primera observación", "Recomendación 1");
        ObservationResponseDTO dto2 = createTestObservationDTO(2L, "Segunda observación", "Recomendación 2");

        when(observationRepository.findAll()).thenReturn(Arrays.asList(observation1, observation2));
        when(observationMapper.toDto(observation1)).thenReturn(dto1);
        when(observationMapper.toDto(observation2)).thenReturn(dto2);

        // Act
        List<ObservationResponseDTO> observations = observationService.getAllObservations();

        // Assert
        assertNotNull(observations);
        assertEquals(2, observations.size());
        assertEquals("Primera observación", observations.get(0).getContent());
        assertEquals("Segunda observación", observations.get(1).getContent());

        verify(observationRepository).findAll();
        verify(observationMapper).toDto(observation1);
        verify(observationMapper).toDto(observation2);
    }

    @Test
    void getAllObservations_WhenNoObservationsExist_ReturnsEmptyList() {
        // Arrange
        when(observationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ObservationResponseDTO> observations = observationService.getAllObservations();

        // Assert
        assertNotNull(observations);
        assertTrue(observations.isEmpty());

        verify(observationRepository).findAll();
        verify(observationMapper, never()).toDto(any());
    }

    @Test
    void createObservation_WhenValidObservation_ReturnsSavedObservationDTO() {
        // Arrange
        Long appointmentResultId = 1L;
        AppointmentResult result = createTestAppointmentResult(appointmentResultId);

        ObservationRequestDTO requestDTO = ObservationRequestDTO.builder()
                .content("Observación del paciente")
                .recommendation("Recomendación para el paciente")
                .appointmentResultId(appointmentResultId)
                .build();

        Observation observation = createTestObservation(null, "Observación del paciente", "Recomendación para el paciente", null);
        Observation savedObservation = createTestObservation(1L, "Observación del paciente", "Recomendación para el paciente", result);
        ObservationResponseDTO responseDTO = createTestObservationDTO(1L, "Observación del paciente", "Recomendación para el paciente");

        when(appointmentResultRepository.findById(appointmentResultId)).thenReturn(Optional.of(result));
        when(observationMapper.toEntity(requestDTO)).thenReturn(observation);
        when(observationRepository.save(observation)).thenReturn(savedObservation);
        when(observationMapper.toDto(savedObservation)).thenReturn(responseDTO);

        // Act
        ObservationResponseDTO createdObservation = observationService.createObservation(requestDTO);

        // Assert
        assertNotNull(createdObservation);
        assertEquals(1L, createdObservation.getId());
        assertEquals("Observación del paciente", createdObservation.getContent());
        assertEquals("Recomendación para el paciente", createdObservation.getRecommendation());

        verify(appointmentResultRepository).findById(appointmentResultId);
        verify(observationMapper).toEntity(requestDTO);
        verify(observationRepository).save(observation);
        verify(observationMapper).toDto(savedObservation);
    }

    @Test
    void createObservation_WhenAppointmentResultNotFound_ThrowsException() {
        // Arrange
        Long nonExistentAppointmentId = 99L;
        ObservationRequestDTO requestDTO = ObservationRequestDTO.builder()
                .appointmentResultId(nonExistentAppointmentId)
                .build();

        when(appointmentResultRepository.findById(nonExistentAppointmentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AppointmentResultNotFoundException.class,
                () -> observationService.createObservation(requestDTO));

        verify(appointmentResultRepository).findById(nonExistentAppointmentId);
        verify(observationRepository, never()).save(any());
    }

    @Test
    void updateObservation_WhenObservationExists_ReturnsUpdatedObservationDTO() {
        // Arrange
        Long id = 1L;
        AppointmentResult result = createTestAppointmentResult(1L);

        ObservationUpdateDTO updateDTO = ObservationUpdateDTO.builder()
                .content("Observación actualizada")
                .recommendation("Recomendación actualizada")
                .build();

        Observation existingObservation = createTestObservation(id, "Observación original", "Recomendación original", result);
        Observation updatedObservation = createTestObservation(id, "Observación actualizada", "Recomendación actualizada", result);
        ObservationResponseDTO responseDTO = createTestObservationDTO(id, "Observación actualizada", "Recomendación actualizada");

        when(observationRepository.findById(id)).thenReturn(Optional.of(existingObservation));
        when(observationRepository.save(existingObservation)).thenReturn(updatedObservation);
        when(observationMapper.toDto(updatedObservation)).thenReturn(responseDTO);

        // Act
        ObservationResponseDTO resultDTO = observationService.updateObservation(id, updateDTO);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(id, resultDTO.getId());
        assertEquals("Observación actualizada", resultDTO.getContent());
        assertEquals("Recomendación actualizada", resultDTO.getRecommendation());

        verify(observationRepository).findById(id);
        verify(observationRepository).save(existingObservation);
        verify(observationMapper).toDto(updatedObservation);
    }

    @Test
    void updateObservation_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 99L;
        ObservationUpdateDTO updateDTO = ObservationUpdateDTO.builder().build();

        when(observationRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ObservationNotFoundException.class,
                () -> observationService.updateObservation(nonExistentId, updateDTO));

        assertEquals("Observation not found with ID: " + nonExistentId, exception.getMessage());
        verify(observationRepository).findById(nonExistentId);
        verify(observationRepository, never()).save(any());
        verify(observationMapper, never()).toDto(any());
    }

    @Test
    void deleteObservation_WhenObservationExists_DeletesObservation() {
        // Arrange
        Long id = 1L;
        AppointmentResult result = createTestAppointmentResult(1L);
        Observation observation = createTestObservation(id, "Contenido", "Recomendación", result);

        when(observationRepository.findById(id)).thenReturn(Optional.of(observation));

        // Act
        observationService.deleteObservation(id);

        // Assert
        verify(observationRepository).findById(id);
        verify(observationRepository).delete(observation);
    }

    @Test
    void deleteObservation_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 99L;
        when(observationRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ObservationNotFoundException.class,
                () -> observationService.deleteObservation(nonExistentId));

        assertEquals("Observation not found with ID: " + nonExistentId, exception.getMessage());
        verify(observationRepository).findById(nonExistentId);
        verify(observationRepository, never()).delete(any());
    }
}