package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.exception.runtime.appointment.MedicalOfficeNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalOfficeMapper;
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

    @Mock
    private MedicalOfficeMapper medicalOfficeMapper;

    @InjectMocks
    private MedicalOfficeServiceImpl medicalOfficeService;

    private MedicalOffice medicalOffice;
    private MedicalOfficeResponseDTO medicalOfficeResponseDTO;
    private MedicalOfficeDTO medicalOfficeDTO;

    @BeforeEach
    void setUp() {
        medicalOffice = new MedicalOffice();
        medicalOffice.setId(1L);
        medicalOffice.setName("Consultorio 1");

        medicalOfficeResponseDTO = new MedicalOfficeResponseDTO();
        medicalOfficeResponseDTO.setId(1L);
        medicalOfficeResponseDTO.setName("Consultorio 1");

        medicalOfficeDTO = new MedicalOfficeDTO();
        medicalOfficeDTO.setName("Consultorio 1");
    }

    @Test
    void getMedicalOfficeById_WhenIdExists_ReturnsOfficeDTO() {
        // Arrange
        when(medicalOfficeRepository.findById(1L)).thenReturn(Optional.of(medicalOffice));
        when(medicalOfficeMapper.toDto(medicalOffice)).thenReturn(medicalOfficeResponseDTO);

        // Act
        MedicalOfficeResponseDTO result = medicalOfficeService.getMedicalOfficeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Consultorio 1", result.getName());
        verify(medicalOfficeRepository, times(1)).findById(1L);
        verify(medicalOfficeMapper, times(1)).toDto(medicalOffice);
    }

    @Test
    void getMedicalOfficeById_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        when(medicalOfficeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalOfficeNotFoundException.class, () -> {
            medicalOfficeService.getMedicalOfficeById(1L);
        });

        verify(medicalOfficeRepository, times(1)).findById(1L);
    }

    @Test
    void getAllMedicalOffices_WhenCalled_ReturnsAllOfficeDTOs() {
        // Arrange
        MedicalOffice office2 = new MedicalOffice();
        office2.setId(2L);
        office2.setName("Consultorio 2");

        MedicalOfficeResponseDTO dto2 = new MedicalOfficeResponseDTO();
        dto2.setId(2L);
        dto2.setName("Consultorio 2");

        when(medicalOfficeRepository.findAll()).thenReturn(Arrays.asList(medicalOffice, office2));
        when(medicalOfficeMapper.toDto(medicalOffice)).thenReturn(medicalOfficeResponseDTO);
        when(medicalOfficeMapper.toDto(office2)).thenReturn(dto2);

        // Act
        List<MedicalOfficeResponseDTO> result = medicalOfficeService.getAllMedicalOffices();

        // Assert
        assertEquals(2, result.size());
        verify(medicalOfficeRepository, times(1)).findAll();
        verify(medicalOfficeMapper, times(1)).toDto(medicalOffice);
        verify(medicalOfficeMapper, times(1)).toDto(office2);
    }

    @Test
    void createMedicalOffice_WhenValidOffice_ReturnsSavedOfficeDTO() {
        // Arrange
        when(medicalOfficeMapper.toEntity(medicalOfficeDTO)).thenReturn(medicalOffice);
        when(medicalOfficeRepository.save(medicalOffice)).thenReturn(medicalOffice);
        when(medicalOfficeMapper.toDto(medicalOffice)).thenReturn(medicalOfficeResponseDTO);

        // Act
        MedicalOfficeResponseDTO result = medicalOfficeService.createMedicalOffice(medicalOfficeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(medicalOfficeResponseDTO, result);
        verify(medicalOfficeRepository, times(1)).save(medicalOffice);
        verify(medicalOfficeMapper, times(1)).toEntity(medicalOfficeDTO);
        verify(medicalOfficeMapper, times(1)).toDto(medicalOffice);
    }

    @Test
    void updateMedicalOffice_WhenIdExists_ReturnsUpdatedOfficeDTO() {
        // Arrange
        MedicalOffice updatedOffice = new MedicalOffice();
        updatedOffice.setId(1L);
        updatedOffice.setName("Consultorio Actualizado");

        MedicalOfficeResponseDTO updatedDTO = new MedicalOfficeResponseDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Consultorio Actualizado");

        when(medicalOfficeRepository.findById(1L)).thenReturn(Optional.of(medicalOffice));
        when(medicalOfficeRepository.save(any(MedicalOffice.class))).thenReturn(updatedOffice);
        when(medicalOfficeMapper.toDto(updatedOffice)).thenReturn(updatedDTO);

        // Act
        MedicalOfficeResponseDTO result = medicalOfficeService.updateMedicalOffice(1L, medicalOfficeDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Consultorio Actualizado", result.getName());
        verify(medicalOfficeRepository, times(1)).findById(1L);
        verify(medicalOfficeRepository, times(1)).save(any(MedicalOffice.class));
    }

    @Test
    void updateMedicalOffice_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        when(medicalOfficeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalOfficeNotFoundException.class, () -> {
            medicalOfficeService.updateMedicalOffice(99L, medicalOfficeDTO);
        });

        verify(medicalOfficeRepository, times(1)).findById(99L);
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
    void deleteMedicalOffice_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        when(medicalOfficeRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(MedicalOfficeNotFoundException.class, () -> {
            medicalOfficeService.deleteMedicalOffice(1L);
        });

        verify(medicalOfficeRepository, times(1)).existsById(1L);
        verify(medicalOfficeRepository, never()).deleteById(any());
    }
}