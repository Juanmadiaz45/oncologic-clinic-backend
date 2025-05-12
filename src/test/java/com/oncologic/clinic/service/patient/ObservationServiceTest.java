package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.ObservationRequestDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.update.ObservationUpdateDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Observation;
import com.oncologic.clinic.exception.runtime.patient.ObservationNotFoundException;
import com.oncologic.clinic.mapper.patient.ObservationMapper;
import com.oncologic.clinic.repository.patient.ObservationRepository;
import com.oncologic.clinic.service.patient.impl.ObservationServiceImpl;
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
public class ObservationServiceTest {
    @Mock
    private ObservationRepository observationRepository;

    @Mock
    private ObservationMapper observationMapper;

    @InjectMocks
    private ObservationServiceImpl observationService;

    @Test
    void getObservationById_WhenObservationExists_ReturnsObservationDTO() {
        // Arrange
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        AppointmentResult appointmentResult = new AppointmentResult();
        appointmentResult.setId(1L);
        appointmentResult.setMedicalHistory(medicalHistory);

        Observation observation = new Observation();
        observation.setId(1L);
        observation.setContent("Paciente presenta mejora notable");
        observation.setRecommendation("Continuar con el tratamiento");
        observation.setAppointmentResult(appointmentResult);

        ObservationResponseDTO responseDTO = new ObservationResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setContent("Paciente presenta mejora notable");
        responseDTO.setRecommendation("Continuar con el tratamiento");

        when(observationRepository.findById(1L)).thenReturn(Optional.of(observation));
        when(observationMapper.toDto(observation)).thenReturn(responseDTO);

        // Act
        ObservationResponseDTO foundObservation = observationService.getObservationById(1L);

        // Assert
        assertNotNull(foundObservation);
        assertEquals(1L, foundObservation.getId());
        assertEquals("Paciente presenta mejora notable", foundObservation.getContent());
        assertEquals("Continuar con el tratamiento", foundObservation.getRecommendation());

        verify(observationRepository, times(1)).findById(1L);
        verify(observationMapper, times(1)).toDto(observation);
    }

    @Test
    void getObservationById_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        when(observationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ObservationNotFoundException.class, () ->
                observationService.getObservationById(99L));

        assertEquals("Observation not found with id: 99", exception.getMessage());

        verify(observationRepository, times(1)).findById(99L);
        verify(observationMapper, never()).toDto(any());
    }

    @Test
    void getAllObservations_WhenObservationsExist_ReturnsObservationsDTOList() {
        // Arrange
        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        AppointmentResult appointmentResult = new AppointmentResult();
        appointmentResult.setId(1L);
        appointmentResult.setMedicalHistory(medicalHistory);

        Observation observation1 = new Observation();
        observation1.setId(1L);
        observation1.setContent("Primera observación");
        observation1.setAppointmentResult(appointmentResult);

        Observation observation2 = new Observation();
        observation2.setId(2L);
        observation2.setContent("Segunda observación");
        observation2.setAppointmentResult(appointmentResult);

        ObservationResponseDTO dto1 = new ObservationResponseDTO();
        dto1.setId(1L);
        dto1.setContent("Primera observación");

        ObservationResponseDTO dto2 = new ObservationResponseDTO();
        dto2.setId(2L);
        dto2.setContent("Segunda observación");

        List<Observation> mockObservations = Arrays.asList(observation1, observation2);
        List<ObservationResponseDTO> mockDTOs = Arrays.asList(dto1, dto2);

        when(observationRepository.findAll()).thenReturn(mockObservations);
        when(observationMapper.toDto(observation1)).thenReturn(dto1);
        when(observationMapper.toDto(observation2)).thenReturn(dto2);

        // Act
        List<ObservationResponseDTO> observations = observationService.getAllObservations();

        // Assert
        assertNotNull(observations);
        assertEquals(2, observations.size());
        assertEquals("Primera observación", observations.get(0).getContent());
        assertEquals("Segunda observación", observations.get(1).getContent());

        verify(observationRepository, times(1)).findAll();
        verify(observationMapper, times(1)).toDto(observation1);
        verify(observationMapper, times(1)).toDto(observation2);
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

        verify(observationRepository, times(1)).findAll();
        verify(observationMapper, never()).toDto(any());
    }

    @Test
    void createObservation_WhenValidObservation_ReturnsSavedObservationDTO() {
        // Arrange
        ObservationRequestDTO requestDTO = new ObservationRequestDTO();
        requestDTO.setContent("Observación del paciente");
        requestDTO.setRecommendation("Recomendación para el paciente");
        requestDTO.setAppointmentResultId(1L);

        AppointmentResult appointmentResult = new AppointmentResult();
        appointmentResult.setId(1L);

        Observation observation = new Observation();
        observation.setContent("Observación del paciente");
        observation.setRecommendation("Recomendación para el paciente");
        observation.setAppointmentResult(appointmentResult);

        Observation savedObservation = new Observation();
        savedObservation.setId(1L);
        savedObservation.setContent("Observación del paciente");
        savedObservation.setRecommendation("Recomendación para el paciente");
        savedObservation.setAppointmentResult(appointmentResult);

        ObservationResponseDTO responseDTO = new ObservationResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setContent("Observación del paciente");
        responseDTO.setRecommendation("Recomendación para el paciente");

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

        verify(observationMapper, times(1)).toEntity(requestDTO);
        verify(observationRepository, times(1)).save(observation);
        verify(observationMapper, times(1)).toDto(savedObservation);
    }

    @Test
    void updateObservation_WhenObservationExists_ReturnsUpdatedObservationDTO() {
        // Arrange
        Long observationId = 1L;

        ObservationUpdateDTO updateDTO = new ObservationUpdateDTO();
        updateDTO.setContent("Observación actualizada");
        updateDTO.setRecommendation("Recomendación actualizada");

        AppointmentResult appointmentResult = new AppointmentResult();
        appointmentResult.setId(1L);

        Observation existingObservation = new Observation();
        existingObservation.setId(observationId);
        existingObservation.setContent("Observación original");
        existingObservation.setRecommendation("Recomendación original");
        existingObservation.setAppointmentResult(appointmentResult);

        Observation updatedObservation = new Observation();
        updatedObservation.setId(observationId);
        updatedObservation.setContent("Observación actualizada");
        updatedObservation.setRecommendation("Recomendación actualizada");
        updatedObservation.setAppointmentResult(appointmentResult);

        ObservationResponseDTO responseDTO = new ObservationResponseDTO();
        responseDTO.setId(observationId);
        responseDTO.setContent("Observación actualizada");
        responseDTO.setRecommendation("Recomendación actualizada");

        when(observationRepository.findById(observationId)).thenReturn(Optional.of(existingObservation));
        when(observationRepository.save(existingObservation)).thenReturn(updatedObservation);
        when(observationMapper.toDto(updatedObservation)).thenReturn(responseDTO);

        // Act
        ObservationResponseDTO result = observationService.updateObservation(observationId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(observationId, result.getId());
        assertEquals("Observación actualizada", result.getContent());
        assertEquals("Recomendación actualizada", result.getRecommendation());

        verify(observationRepository, times(1)).findById(observationId);
        verify(observationRepository, times(1)).save(existingObservation);
        verify(observationMapper, times(1)).toDto(updatedObservation);
    }

    @Test
    void updateObservation_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 99L;
        ObservationUpdateDTO updateDTO = new ObservationUpdateDTO();
        updateDTO.setContent("Contenido");

        when(observationRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ObservationNotFoundException.class, () ->
                observationService.updateObservation(nonExistentId, updateDTO));

        assertEquals("Observation not found with id: " + nonExistentId, exception.getMessage());

        verify(observationRepository, times(1)).findById(nonExistentId);
        verify(observationRepository, never()).save(any());
        verify(observationMapper, never()).toDto(any());
    }

    @Test
    void deleteObservation_WhenObservationExists_DeletesObservation() {
        // Arrange
        Long observationId = 1L;
        Observation observation = new Observation();
        observation.setId(observationId);
        observation.setContent("Contenido");

        when(observationRepository.findById(observationId)).thenReturn(Optional.of(observation));

        // Act
        observationService.deleteObservation(observationId);

        // Assert
        verify(observationRepository, times(1)).findById(observationId);
        verify(observationRepository, times(1)).delete(observation);
    }

    @Test
    void deleteObservation_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        Long nonExistentId = 99L;
        when(observationRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ObservationNotFoundException.class, () ->
                observationService.deleteObservation(nonExistentId));

        assertEquals("Observation not found with id: " + nonExistentId, exception.getMessage());

        verify(observationRepository, times(1)).findById(nonExistentId);
        verify(observationRepository, never()).delete(any());
    }
}