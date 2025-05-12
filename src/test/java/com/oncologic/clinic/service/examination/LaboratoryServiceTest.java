package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.LaboratoryDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import com.oncologic.clinic.exception.runtime.examination.LaboratoryNotFoundException;
import com.oncologic.clinic.mapper.examination.LaboratoryMapper;
import com.oncologic.clinic.repository.examination.LaboratoryRepository;
import com.oncologic.clinic.service.examination.impl.LaboratoryServiceImpl;
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
public class LaboratoryServiceTest {

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @Mock
    private LaboratoryMapper laboratoryMapper;

    @InjectMocks
    private LaboratoryServiceImpl laboratoryService;

    private Laboratory laboratory;
    private LaboratoryResponseDTO laboratoryResponseDTO;
    private LaboratoryDTO laboratoryDTO;

    @BeforeEach
    void setUp() {
        laboratory = new Laboratory();
        laboratory.setId(1L);
        laboratory.setName("Lab Corp");

        laboratoryResponseDTO = new LaboratoryResponseDTO();
        laboratoryResponseDTO.setId(1L);
        laboratoryResponseDTO.setName("Lab Corp");

        laboratoryDTO = new LaboratoryDTO();
        laboratoryDTO.setName("Lab Corp");
    }

    @Test
    void getLaboratoryById_WhenLabExists_ReturnsLaboratoryDTO() {
        // Arrange
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratory));
        when(laboratoryMapper.toDto(laboratory)).thenReturn(laboratoryResponseDTO);

        // Act
        LaboratoryResponseDTO result = laboratoryService.getLaboratoryById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Lab Corp", result.getName());
        verify(laboratoryRepository, times(1)).findById(1L);
        verify(laboratoryMapper, times(1)).toDto(laboratory);
    }

    @Test
    void getLaboratoryById_WhenLabDoesNotExist_ThrowsException() {
        // Arrange
        when(laboratoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LaboratoryNotFoundException.class, () ->
                laboratoryService.getLaboratoryById(99L));
        verify(laboratoryRepository, times(1)).findById(99L);
    }

    @Test
    void getAllLaboratories_WhenLabsExist_ReturnsLaboratoryDTOList() {
        // Arrange
        Laboratory lab2 = new Laboratory();
        lab2.setId(2L);
        lab2.setName("Another Lab");

        LaboratoryResponseDTO responseDTO2 = new LaboratoryResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setName("Another Lab");

        when(laboratoryRepository.findAll()).thenReturn(Arrays.asList(laboratory, lab2));
        when(laboratoryMapper.toDto(laboratory)).thenReturn(laboratoryResponseDTO);
        when(laboratoryMapper.toDto(lab2)).thenReturn(responseDTO2);

        // Act
        List<LaboratoryResponseDTO> results = laboratoryService.getAllLaboratories();

        // Assert
        assertEquals(2, results.size());
        verify(laboratoryRepository, times(1)).findAll();
        verify(laboratoryMapper, times(1)).toDto(laboratory);
        verify(laboratoryMapper, times(1)).toDto(lab2);
    }

    @Test
    void createLaboratory_WhenValidRequest_ReturnsSavedLaboratoryDTO() {
        // Arrange
        when(laboratoryMapper.toEntity(laboratoryDTO)).thenReturn(laboratory);
        when(laboratoryRepository.save(laboratory)).thenReturn(laboratory);
        when(laboratoryMapper.toDto(laboratory)).thenReturn(laboratoryResponseDTO);

        // Act
        LaboratoryResponseDTO result = laboratoryService.createLaboratory(laboratoryDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Lab Corp", result.getName());
        verify(laboratoryRepository, times(1)).save(laboratory);
        verify(laboratoryMapper, times(1)).toEntity(laboratoryDTO);
        verify(laboratoryMapper, times(1)).toDto(laboratory);
    }

    @Test
    void updateLaboratory_WhenLabExists_ReturnsUpdatedLaboratoryDTO() {
        // Arrange
        Laboratory updatedLab = new Laboratory();
        updatedLab.setId(1L);
        updatedLab.setName("Updated Lab");

        LaboratoryResponseDTO updatedResponse = new LaboratoryResponseDTO();
        updatedResponse.setId(1L);
        updatedResponse.setName("Updated Lab");

        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratory));
        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(updatedLab);
        when(laboratoryMapper.toDto(updatedLab)).thenReturn(updatedResponse);

        // Act
        LaboratoryResponseDTO result = laboratoryService.updateLaboratory(1L, laboratoryDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Lab", result.getName());
        verify(laboratoryRepository, times(1)).findById(1L);
        verify(laboratoryRepository, times(1)).save(any(Laboratory.class));
    }

    @Test
    void updateLaboratory_WhenLabDoesNotExist_ThrowsException() {
        // Arrange
        when(laboratoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LaboratoryNotFoundException.class, () ->
                laboratoryService.updateLaboratory(99L, laboratoryDTO));
        verify(laboratoryRepository, times(1)).findById(99L);
        verify(laboratoryRepository, never()).save(any());
    }

    @Test
    void deleteLaboratory_WhenLabExists_DeletesLab() {
        // Arrange
        when(laboratoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(laboratoryRepository).deleteById(1L);

        // Act
        laboratoryService.deleteLaboratory(1L);

        // Assert
        verify(laboratoryRepository, times(1)).existsById(1L);
        verify(laboratoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteLaboratory_WhenLabDoesNotExist_ThrowsException() {
        // Arrange
        when(laboratoryRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(LaboratoryNotFoundException.class, () ->
                laboratoryService.deleteLaboratory(99L));
        verify(laboratoryRepository, times(1)).existsById(99L);
        verify(laboratoryRepository, never()).deleteById(any());
    }
}