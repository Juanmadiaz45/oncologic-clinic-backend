/*package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.service.appointment.impl.MedicalAppointmentServiceImpl;
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
class MedicalAppointmentServiceTest {

    @Mock
    private MedicalAppointmentRepository medicalAppointmentRepository;

    @InjectMocks
    private MedicalAppointmentServiceImpl medicalAppointmentService;

    private MedicalAppointment medicalAppointment;

    @BeforeEach
    void setUp() {
        medicalAppointment = new MedicalAppointment();
        medicalAppointment.setId(1L);
        medicalAppointment.setAppointmentDate(LocalDateTime.now());
        medicalAppointment.setId3(12345L);
    }

    @Test
    void getMedicalAppointmentById_whenIdExists_shouldReturnAppointment() {
        // Arrange
        when(medicalAppointmentRepository.findById(1L)).thenReturn(Optional.of(medicalAppointment));

        // Act
        MedicalAppointment result = medicalAppointmentService.getMedicalAppointmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicalAppointmentRepository, times(1)).findById(1L);
    }

    @Test
    void getMedicalAppointmentById_whenIdDoesNotExist_shouldReturnNull() {
        // Arrange
        when(medicalAppointmentRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        MedicalAppointment result = medicalAppointmentService.getMedicalAppointmentById(1L);

        // Assert
        assertNull(result);
        verify(medicalAppointmentRepository, times(1)).findById(1L);
    }

    @Test
    void getAllMedicalAppointments_whenAppointmentsExist_shouldReturnList() {
        // Arrange
        MedicalAppointment appointment2 = new MedicalAppointment();
        appointment2.setId(2L);
        List<MedicalAppointment> expectedAppointments = Arrays.asList(medicalAppointment, appointment2);

        when(medicalAppointmentRepository.findAll()).thenReturn(expectedAppointments);

        // Act
        List<MedicalAppointment> result = medicalAppointmentService.getAllMedicalAppointments();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.containsAll(expectedAppointments));
        verify(medicalAppointmentRepository, times(1)).findAll();
    }

    @Test
    void createMedicalAppointment_whenValidAppointment_shouldSaveAndReturn() {
        // Arrange
        when(medicalAppointmentRepository.save(medicalAppointment)).thenReturn(medicalAppointment);

        // Act
        MedicalAppointment result = medicalAppointmentService.createMedicalAppointment(medicalAppointment);

        // Assert
        assertNotNull(result);
        assertEquals(medicalAppointment, result);
        verify(medicalAppointmentRepository, times(1)).save(medicalAppointment);
    }

    @Test
    void updateMedicalAppointment_whenIdExists_shouldUpdateAndReturnAppointment() {
        // Arrange
        MedicalAppointment updatedAppointment = new MedicalAppointment();
        updatedAppointment.setId(1L);
        updatedAppointment.setAppointmentDate(LocalDateTime.now().plusDays(1));
        updatedAppointment.setId3(54321L);

        when(medicalAppointmentRepository.existsById(1L)).thenReturn(true);
        when(medicalAppointmentRepository.save(updatedAppointment)).thenReturn(updatedAppointment);

        // Act
        MedicalAppointment result = medicalAppointmentService.updateMedicalAppointment(updatedAppointment);

        // Assert
        assertNotNull(result);
        assertEquals(54321L, result.getId3());
        verify(medicalAppointmentRepository, times(1)).existsById(1L);
        verify(medicalAppointmentRepository, times(1)).save(updatedAppointment);
    }

    @Test
    void updateMedicalAppointment_whenIdDoesNotExist_shouldReturnNull() {
        // Arrange
        MedicalAppointment updatedAppointment = new MedicalAppointment();
        updatedAppointment.setId(99L);

        when(medicalAppointmentRepository.existsById(99L)).thenReturn(false);

        // Act
        MedicalAppointment result = medicalAppointmentService.updateMedicalAppointment(updatedAppointment);

        // Assert
        assertNull(result);
        verify(medicalAppointmentRepository, times(1)).existsById(99L);
        verify(medicalAppointmentRepository, never()).save(any());
    }

    @Test
    void deleteMedicalAppointment_whenIdExists_shouldDeleteAppointment() {
        // Arrange
        when(medicalAppointmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicalAppointmentRepository).deleteById(1L);

        // Act
        medicalAppointmentService.deleteMedicalAppointment(1L);

        // Assert
        verify(medicalAppointmentRepository, times(1)).existsById(1L);
        verify(medicalAppointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedicalAppointment_whenIdDoesNotExist_shouldNotDelete() {
        // Arrange
        when(medicalAppointmentRepository.existsById(1L)).thenReturn(false);

        // Act
        medicalAppointmentService.deleteMedicalAppointment(1L);

        // Assert
        verify(medicalAppointmentRepository, times(1)).existsById(1L);
        verify(medicalAppointmentRepository, never()).deleteById(any());
    }
}
 */
