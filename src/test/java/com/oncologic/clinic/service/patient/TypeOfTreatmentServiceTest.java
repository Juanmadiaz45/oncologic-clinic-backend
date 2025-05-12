package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.TypeOfTreatmentRequestDTO;
import com.oncologic.clinic.dto.patient.response.TypeOfTreatmentResponseDTO;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.entity.patient.TypeOfTreatment;
import com.oncologic.clinic.exception.runtime.patient.ResourceNotFoundException;
import com.oncologic.clinic.mapper.patient.TypeOfTreatmentMapper;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.repository.patient.TypeOfTreatmentRepository;
import com.oncologic.clinic.service.patient.impl.TypeOfTreatmentServiceImpl;
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
public class TypeOfTreatmentServiceTest {

    @Mock
    private TypeOfTreatmentRepository typeOfTreatmentRepository;

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private TypeOfTreatmentMapper typeOfTreatmentMapper;

    @InjectMocks
    private TypeOfTreatmentServiceImpl typeOfTreatmentService;

    private TypeOfTreatment createTestType(Long id, String name, Treatment treatment) {
        TypeOfTreatment type = new TypeOfTreatment();
        type.setId(id);
        type.setName(name);
        type.setTreatment(treatment);
        return type;
    }

    private TypeOfTreatmentResponseDTO createTestTypeDTO(Long id, String name) {
        return TypeOfTreatmentResponseDTO.builder()
                .id(id)
                .name(name)
                .build();
    }

    private Treatment createTestTreatment(Long id) {
        Treatment treatment = new Treatment();
        treatment.setId(id);
        return treatment;
    }

    @Test
    void getTypeOfTreatmentById_WhenIdExists_ReturnsTypeOfTreatmentDTO() {
        // Arrange
        Long id = 1L;
        Treatment treatment = createTestTreatment(1L);
        TypeOfTreatment type = createTestType(id, "Surgery", treatment);
        TypeOfTreatmentResponseDTO expectedDTO = createTestTypeDTO(id, "Surgery");

        when(typeOfTreatmentRepository.findById(id)).thenReturn(Optional.of(type));
        when(typeOfTreatmentMapper.toDto(type)).thenReturn(expectedDTO);

        // Act
        TypeOfTreatmentResponseDTO result = typeOfTreatmentService.getTypeOfTreatmentById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Surgery", result.getName());

        verify(typeOfTreatmentRepository, times(1)).findById(id);
        verify(typeOfTreatmentMapper, times(1)).toDto(type);
    }

    @Test
    void getTypeOfTreatmentById_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(typeOfTreatmentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> typeOfTreatmentService.getTypeOfTreatmentById(id));

        verify(typeOfTreatmentRepository, times(1)).findById(id);
        verify(typeOfTreatmentMapper, never()).toDto(any());
    }

    @Test
    void getAllTypesOfTreatment_WhenCalled_ReturnsTypeOfTreatmentDTOList() {
        // Arrange
        Treatment treatment = createTestTreatment(1L);
        TypeOfTreatment type1 = createTestType(1L, "Type 1", treatment);
        TypeOfTreatment type2 = createTestType(2L, "Type 2", treatment);

        TypeOfTreatmentResponseDTO dto1 = createTestTypeDTO(1L, "Type 1");
        TypeOfTreatmentResponseDTO dto2 = createTestTypeDTO(2L, "Type 2");

        when(typeOfTreatmentRepository.findAll()).thenReturn(Arrays.asList(type1, type2));
        when(typeOfTreatmentMapper.toDto(type1)).thenReturn(dto1);
        when(typeOfTreatmentMapper.toDto(type2)).thenReturn(dto2);

        // Act
        List<TypeOfTreatmentResponseDTO> result = typeOfTreatmentService.getAllTypesOfTreatment();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Type 1", result.get(0).getName());
        assertEquals("Type 2", result.get(1).getName());

        verify(typeOfTreatmentRepository, times(1)).findAll();
        verify(typeOfTreatmentMapper, times(1)).toDto(type1);
        verify(typeOfTreatmentMapper, times(1)).toDto(type2);
    }

    @Test
    void createTypeOfTreatment_WhenValidData_ReturnsTypeOfTreatmentDTO() {
        // Arrange
        Long treatmentId = 1L;
        Treatment treatment = createTestTreatment(treatmentId);

        TypeOfTreatmentRequestDTO requestDTO = TypeOfTreatmentRequestDTO.builder()
                .name("Immunotherapy")
                .treatmentId(treatmentId)
                .build();

        TypeOfTreatment typeToSave = createTestType(null, "Immunotherapy", treatment);
        TypeOfTreatment savedType = createTestType(1L, "Immunotherapy", treatment);
        TypeOfTreatmentResponseDTO responseDTO = createTestTypeDTO(1L, "Immunotherapy");

        when(treatmentRepository.findById(treatmentId)).thenReturn(Optional.of(treatment));
        when(typeOfTreatmentMapper.toEntity(requestDTO)).thenReturn(typeToSave);
        when(typeOfTreatmentRepository.save(typeToSave)).thenReturn(savedType);
        when(typeOfTreatmentMapper.toDto(savedType)).thenReturn(responseDTO);

        // Act
        TypeOfTreatmentResponseDTO result = typeOfTreatmentService.createTypeOfTreatment(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Immunotherapy", result.getName());

        verify(treatmentRepository, times(1)).findById(treatmentId);
        verify(typeOfTreatmentMapper, times(1)).toEntity(requestDTO);
        verify(typeOfTreatmentRepository, times(1)).save(typeToSave);
        verify(typeOfTreatmentMapper, times(1)).toDto(savedType);
    }

    @Test
    void updateTypeOfTreatment_WhenTypeExists_ReturnsUpdatedTypeDTO() {
        // Arrange
        Long id = 1L;
        Long newTreatmentId = 2L;
        Treatment oldTreatment = createTestTreatment(1L);
        Treatment newTreatment = createTestTreatment(newTreatmentId);

        TypeOfTreatmentRequestDTO updateDTO = TypeOfTreatmentRequestDTO.builder()
                .name("Updated Type")
                .treatmentId(newTreatmentId)
                .build();

        TypeOfTreatment existingType = createTestType(id, "Old Type", oldTreatment);
        TypeOfTreatment updatedType = createTestType(id, "Updated Type", newTreatment);
        TypeOfTreatmentResponseDTO responseDTO = createTestTypeDTO(id, "Updated Type");

        when(typeOfTreatmentRepository.findById(id)).thenReturn(Optional.of(existingType));
        when(treatmentRepository.findById(newTreatmentId)).thenReturn(Optional.of(newTreatment));
        when(typeOfTreatmentRepository.save(existingType)).thenReturn(updatedType);
        when(typeOfTreatmentMapper.toDto(updatedType)).thenReturn(responseDTO);

        // Act
        TypeOfTreatmentResponseDTO result = typeOfTreatmentService.updateTypeOfTreatment(id, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Type", result.getName());

        verify(typeOfTreatmentRepository, times(1)).findById(id);
        verify(treatmentRepository, times(1)).findById(newTreatmentId);
        verify(typeOfTreatmentRepository, times(1)).save(existingType);
        verify(typeOfTreatmentMapper, times(1)).toDto(updatedType);
    }

    @Test
    void deleteTypeOfTreatment_WhenIdExists_DeletesType() {
        // Arrange
        Long id = 1L;
        Treatment treatment = createTestTreatment(1L);
        TypeOfTreatment type = createTestType(id, "Type", treatment);

        when(typeOfTreatmentRepository.findById(id)).thenReturn(Optional.of(type));

        // Act
        typeOfTreatmentService.deleteTypeOfTreatment(id);

        // Assert
        verify(typeOfTreatmentRepository, times(1)).findById(id);
        verify(typeOfTreatmentRepository, times(1)).delete(type);
    }
}