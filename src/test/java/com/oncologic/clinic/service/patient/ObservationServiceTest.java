package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Observation;
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

    @InjectMocks
    private ObservationServiceImpl observationService;

    @Test
    void getObservationById_WhenObservationExists_ReturnsObservation() {
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

        when(observationRepository.findById(1L)).thenReturn(Optional.of(observation));

        // Act
        Observation foundObservation = observationService.getObservationById(1L);

        // Assert
        assertNotNull(foundObservation);
        assertEquals(1L, foundObservation.getId());
        assertEquals("Paciente presenta mejora notable", foundObservation.getContent());
        assertEquals("Continuar con el tratamiento", foundObservation.getRecommendation());
        assertEquals(appointmentResult, foundObservation.getAppointmentResult());

        verify(observationRepository, times(1)).findById(1L);
    }

    @Test
    void getObservationById_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        when(observationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                observationService.getObservationById(99L));

        assertEquals("Observación no encontrada", exception.getMessage());

        verify(observationRepository, times(1)).findById(99L);
    }

    @Test
    void getAllObservations_WhenObservationsExist_ReturnsObservationsList() {
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

        List<Observation> mockObservations = Arrays.asList(observation1, observation2);

        when(observationRepository.findAll()).thenReturn(mockObservations);

        // Act
        List<Observation> observations = observationService.getAllObservations();

        // Assert
        assertNotNull(observations);
        assertEquals(2, observations.size());
        assertEquals("Primera observación", observations.get(0).getContent());
        assertEquals("Segunda observación", observations.get(1).getContent());

        verify(observationRepository, times(1)).findAll();
    }

    @Test
    void getAllObservations_WhenNoObservationsExist_ReturnsEmptyList() {
        // Arrange
        when(observationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Observation> observations = observationService.getAllObservations();

        // Assert
        assertNotNull(observations);
        assertTrue(observations.isEmpty());

        verify(observationRepository, times(1)).findAll();
    }

    @Test
    void createObservation_WhenValidObservation_ReturnsSavedObservation() {
        // Arrange
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

        when(observationRepository.save(observation)).thenReturn(savedObservation);

        // Act
        Observation createdObservation = observationService.createObservation(observation);

        // Assert
        assertNotNull(createdObservation);
        assertEquals(1L, createdObservation.getId());
        assertEquals("Observación del paciente", createdObservation.getContent());
        assertEquals("Recomendación para el paciente", createdObservation.getRecommendation());

        verify(observationRepository, times(1)).save(observation);
    }

    @Test
    void createObservation_WhenNullObservationProvided_ThrowsIllegalArgumentException() {
        // Arrange - No necesario

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                observationService.createObservation(null));

        verify(observationRepository, never()).save(any());
    }

    @Test
    void updateObservation_WhenObservationExists_ReturnsUpdatedObservation() {
        // Arrange
        AppointmentResult appointmentResult = new AppointmentResult();
        appointmentResult.setId(1L);

        Observation existingObservation = new Observation();
        existingObservation.setId(1L);
        existingObservation.setContent("Observación original");
        existingObservation.setRecommendation("Recomendación original");
        existingObservation.setAppointmentResult(appointmentResult);

        Observation updatedObservation = new Observation();
        updatedObservation.setId(1L);
        updatedObservation.setContent("Observación actualizada");
        updatedObservation.setRecommendation("Recomendación actualizada");
        updatedObservation.setAppointmentResult(appointmentResult);

        when(observationRepository.findById(1L)).thenReturn(Optional.of(existingObservation));
        when(observationRepository.save(any(Observation.class))).thenReturn(updatedObservation);

        // Act
        Observation result = observationService.updateObservation(updatedObservation);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Observación actualizada", result.getContent());
        assertEquals("Recomendación actualizada", result.getRecommendation());

        verify(observationRepository, times(1)).findById(1L);
        verify(observationRepository, times(1)).save(existingObservation);
    }

    @Test
    void updateObservation_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        AppointmentResult appointmentResult = new AppointmentResult();
        appointmentResult.setId(1L);

        Observation nonExistentObservation = new Observation();
        nonExistentObservation.setId(99L);
        nonExistentObservation.setContent("Contenido");
        nonExistentObservation.setAppointmentResult(appointmentResult);

        when(observationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                observationService.updateObservation(nonExistentObservation));

        assertEquals("Observación no encontrada", exception.getMessage());

        verify(observationRepository, times(1)).findById(99L);
        verify(observationRepository, never()).save(any(Observation.class));
    }

    @Test
    void deleteObservation_WhenObservationExists_DeletesObservation() {
        // Arrange
        AppointmentResult appointmentResult = new AppointmentResult();
        appointmentResult.setId(1L);

        Observation observation = new Observation();
        observation.setId(1L);
        observation.setContent("Contenido");
        observation.setAppointmentResult(appointmentResult);

        when(observationRepository.findById(1L)).thenReturn(Optional.of(observation));

        // Act
        observationService.deleteObservation(1L);

        // Assert
        verify(observationRepository, times(1)).findById(1L);
        verify(observationRepository, times(1)).delete(observation);
    }

    @Test
    void deleteObservation_WhenObservationDoesNotExist_ThrowsException() {
        // Arrange
        when(observationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () ->
                observationService.deleteObservation(99L));

        assertEquals("Observación no encontrada", exception.getMessage());

        verify(observationRepository, times(1)).findById(99L);
        verify(observationRepository, never()).delete(any(Observation.class));
    }
}