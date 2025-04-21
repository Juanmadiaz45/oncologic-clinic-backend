package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.repository.appointment.MedicalOfficeRepository;
import com.oncologic.clinic.service.appointment.impl.MedicalOfficeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
class MedicalOfficeServiceTest {

    @Mock
    private MedicalOfficeRepository medicalOfficeRepository;

    @InjectMocks
    private MedicalOfficeServiceImpl medicalOfficeService;

    private MedicalOffice medicalOffice;

    @BeforeEach
    void setUp() {
        medicalOffice = new MedicalOffice();
        medicalOffice.setId(1L);
        medicalOffice.setName("Consultorio 1");
    }

    @Test
    void getMedicalOfficeById_WhenIdExists_ReturnsOffice() {
        // Arrange
        when(medicalOfficeRepository.findById(1L)).thenReturn(Optional.of(medicalOffice));

        // Act
        MedicalOffice result = medicalOfficeService.getMedicalOfficeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Consultorio 1", result.getName());
        verify(medicalOfficeRepository, times(1)).findById(1L);
    }

    @Test
    void getMedicalOfficeById_WhenIdDoesNotExist_ReturnsNull() {
        // Arrange
        when(medicalOfficeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        MedicalOffice result = medicalOfficeService.getMedicalOfficeById(1L);

        // Assert
        assertNull(result);
        verify(medicalOfficeRepository, times(1)).findById(1L);
    }

    @Test
    void getAllMedicalOffices_WhenCalled_ReturnsAllOffices() {
        // Arrange
        MedicalOffice office2 = new MedicalOffice();
        office2.setId(2L);
        office2.setName("Consultorio 2");
        when(medicalOfficeRepository.findAll()).thenReturn(Arrays.asList(medicalOffice, office2));

        // Act
        List<MedicalOffice> result = medicalOfficeService.getAllMedicalOffices();

        // Assert
        assertEquals(2, result.size());
        verify(medicalOfficeRepository, times(1)).findAll();
    }

    @Test
    void createMedicalOffice_WhenValidOffice_ReturnsSavedOffice() {
        // Arrange
        when(medicalOfficeRepository.save(medicalOffice)).thenReturn(medicalOffice);

        // Act
        MedicalOffice result = medicalOfficeService.createMedicalOffice(medicalOffice);

        // Assert
        assertNotNull(result);
        assertEquals(medicalOffice, result);
        verify(medicalOfficeRepository, times(1)).save(medicalOffice);
    }

    @Test
    void updateMedicalOffice_WhenIdExists_ReturnsUpdatedOffice() {
        // Arrange
        MedicalOffice updatedOffice = new MedicalOffice();
        updatedOffice.setId(1L);
        updatedOffice.setName("Consultorio Actualizado");

        when(medicalOfficeRepository.existsById(1L)).thenReturn(true);
        when(medicalOfficeRepository.save(updatedOffice)).thenReturn(updatedOffice);

        // Act
        MedicalOffice result = medicalOfficeService.updateMedicalOffice(updatedOffice);

        // Assert
        assertNotNull(result);
        assertEquals("Consultorio Actualizado", result.getName());
        verify(medicalOfficeRepository, times(1)).existsById(1L);
        verify(medicalOfficeRepository, times(1)).save(updatedOffice);
    }

    @Test
    void updateMedicalOffice_WhenIdDoesNotExist_ReturnsNull() {
        // Arrange
        MedicalOffice updatedOffice = new MedicalOffice();
        updatedOffice.setId(99L);
        updatedOffice.setName("Consultorio Inexistente");

        when(medicalOfficeRepository.existsById(99L)).thenReturn(false);

        // Act
        MedicalOffice result = medicalOfficeService.updateMedicalOffice(updatedOffice);

        // Assert
        assertNull(result);
        verify(medicalOfficeRepository, times(1)).existsById(99L);
        verify(medicalOfficeRepository, never()).save(any());
    }

    @Test
    void deleteMedicalOffice_WhenIdExists_DeletesOffice() {
        // Arrange
        when(medicalOfficeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicalOfficeRepository).deleteById(1L);

        // Act
        medicalOfficeService.deleteMedicalOffice(1L);

        // Assert
        verify(medicalOfficeRepository, times(1)).existsById(1L);
        verify(medicalOfficeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedicalOffice_WhenIdDoesNotExist_DoesNothing() {
        // Arrange
        when(medicalOfficeRepository.existsById(1L)).thenReturn(false);

        // Act
        medicalOfficeService.deleteMedicalOffice(1L);

        // Assert
        verify(medicalOfficeRepository, times(1)).existsById(1L);
        verify(medicalOfficeRepository, never()).deleteById(any());
    }
}
