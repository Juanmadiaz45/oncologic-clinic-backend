package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.TreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.dto.patient.update.TreatmentUpdateDTO;
import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.exception.runtime.patient.ResourceNotFoundException;
import com.oncologic.clinic.mapper.patient.TreatmentMapper;
import com.oncologic.clinic.repository.patient.AppointmentResultRepository;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.service.patient.impl.TreatmentServiceImpl;
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
public class TreatmentServiceTest {

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private AppointmentResultRepository appointmentResultRepository;

    @Mock
    private TreatmentMapper treatmentMapper;

    @InjectMocks
    private TreatmentServiceImpl treatmentService;

    private Treatment createTestTreatment(Long id, String name, String description) {
        Treatment treatment = new Treatment();
        treatment.setId(id);
        treatment.setName(name);
        treatment.setDescription(description);
        return treatment;
    }

    private TreatmentResponseDTO createTestTreatmentDTO(Long id, String name, String description) {
        return TreatmentResponseDTO.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
    }

    private AppointmentResult createTestAppointmentResult(Long id) {
        AppointmentResult result = new AppointmentResult();
        result.setId(id);
        return result;
    }

    @Test
    void getTreatmentById_WhenIdExists_ReturnsTreatmentDTO() {
        // Arrange
        Long id = 1L;
        Treatment treatment = createTestTreatment(id, "Chemotherapy", "Cancer treatment");
        TreatmentResponseDTO expectedDTO = createTestTreatmentDTO(id, "Chemotherapy", "Cancer treatment");

        when(treatmentRepository.findById(id)).thenReturn(Optional.of(treatment));
        when(treatmentMapper.toDto(treatment)).thenReturn(expectedDTO);

        // Act
        TreatmentResponseDTO result = treatmentService.getTreatmentById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Chemotherapy", result.getName());
        assertEquals("Cancer treatment", result.getDescription());

        verify(treatmentRepository, times(1)).findById(id);
        verify(treatmentMapper, times(1)).toDto(treatment);
    }

    @Test
    void getTreatmentById_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(treatmentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> treatmentService.getTreatmentById(id));

        verify(treatmentRepository, times(1)).findById(id);
        verify(treatmentMapper, never()).toDto(any());
    }

    @Test
    void getAllTreatments_WhenCalled_ReturnsTreatmentDTOList() {
        // Arrange
        Treatment treatment1 = createTestTreatment(1L, "Treatment 1", "Desc 1");
        Treatment treatment2 = createTestTreatment(2L, "Treatment 2", "Desc 2");

        TreatmentResponseDTO dto1 = createTestTreatmentDTO(1L, "Treatment 1", "Desc 1");
        TreatmentResponseDTO dto2 = createTestTreatmentDTO(2L, "Treatment 2", "Desc 2");

        when(treatmentRepository.findAll()).thenReturn(Arrays.asList(treatment1, treatment2));
        when(treatmentMapper.toDto(treatment1)).thenReturn(dto1);
        when(treatmentMapper.toDto(treatment2)).thenReturn(dto2);

        // Act
        List<TreatmentResponseDTO> result = treatmentService.getAllTreatments();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Treatment 1", result.get(0).getName());
        assertEquals("Treatment 2", result.get(1).getName());

        verify(treatmentRepository, times(1)).findAll();
        verify(treatmentMapper, times(1)).toDto(treatment1);
        verify(treatmentMapper, times(1)).toDto(treatment2);
    }

    @Test
    void createTreatment_WhenValidData_ReturnsTreatmentDTO() {
        // Arrange
        Long appointmentResultId = 1L;
        AppointmentResult appointmentResult = createTestAppointmentResult(appointmentResultId);

        TreatmentRequestDTO requestDTO = TreatmentRequestDTO.builder()
                .name("Radiotherapy")
                .description("Radiation treatment")
                .appointmentResultId(appointmentResultId)
                .build();

        Treatment treatmentToSave = createTestTreatment(null, "Radiotherapy", "Radiation treatment");
        Treatment savedTreatment = createTestTreatment(1L, "Radiotherapy", "Radiation treatment");
        TreatmentResponseDTO responseDTO = createTestTreatmentDTO(1L, "Radiotherapy", "Radiation treatment");

        when(appointmentResultRepository.findById(appointmentResultId)).thenReturn(Optional.of(appointmentResult));
        when(treatmentMapper.toEntity(requestDTO)).thenReturn(treatmentToSave);
        when(treatmentRepository.save(treatmentToSave)).thenReturn(savedTreatment);
        when(treatmentMapper.toDto(savedTreatment)).thenReturn(responseDTO);

        // Act
        TreatmentResponseDTO result = treatmentService.createTreatment(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Radiotherapy", result.getName());
        assertEquals("Radiation treatment", result.getDescription());

        verify(appointmentResultRepository, times(1)).findById(appointmentResultId);
        verify(treatmentMapper, times(1)).toEntity(requestDTO);
        verify(treatmentRepository, times(1)).save(treatmentToSave);
        verify(treatmentMapper, times(1)).toDto(savedTreatment);
    }

    @Test
    void updateTreatment_WhenTreatmentExists_ReturnsUpdatedTreatmentDTO() {
        // Arrange
        Long id = 1L;
        TreatmentUpdateDTO updateDTO = TreatmentUpdateDTO.builder()
                .name("Updated Treatment")
                .description("Updated Description")
                .build();

        Treatment existingTreatment = createTestTreatment(id, "Old Treatment", "Old Description");
        Treatment updatedTreatment = createTestTreatment(id, "Updated Treatment", "Updated Description");
        TreatmentResponseDTO responseDTO = createTestTreatmentDTO(id, "Updated Treatment", "Updated Description");

        when(treatmentRepository.findById(id)).thenReturn(Optional.of(existingTreatment));
        when(treatmentRepository.save(existingTreatment)).thenReturn(updatedTreatment);
        when(treatmentMapper.toDto(updatedTreatment)).thenReturn(responseDTO);

        // Act
        TreatmentResponseDTO result = treatmentService.updateTreatment(id, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Treatment", result.getName());
        assertEquals("Updated Description", result.getDescription());

        verify(treatmentRepository, times(1)).findById(id);
        verify(treatmentRepository, times(1)).save(existingTreatment);
        verify(treatmentMapper, times(1)).toDto(updatedTreatment);
    }

    @Test
    void deleteTreatment_WhenIdExists_DeletesTreatment() {
        // Arrange
        Long id = 1L;
        Treatment treatment = createTestTreatment(id, "Treatment", "Description");

        when(treatmentRepository.findById(id)).thenReturn(Optional.of(treatment));

        // Act
        treatmentService.deleteTreatment(id);

        // Assert
        verify(treatmentRepository, times(1)).findById(id);
        verify(treatmentRepository, times(1)).delete(treatment);
    }
}