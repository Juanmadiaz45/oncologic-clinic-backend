/*package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.entity.patient.TypeOfTreatment;
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

    @InjectMocks
    private TypeOfTreatmentServiceImpl typeOfTreatmentService;

    @Test
    void getTypeOfTreatmentById_WhenIdExists_ReturnsTypeOfTreatment() {
        // Arrange
        Long id = 1L;
        TypeOfTreatment expectedType = new TypeOfTreatment();
        expectedType.setId(id);
        expectedType.setName("Surgery");

        when(typeOfTreatmentRepository.findById(id)).thenReturn(Optional.of(expectedType));

        // Act
        TypeOfTreatment result = typeOfTreatmentService.getTypeOfTreatmentById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Surgery", result.getName());

        verify(typeOfTreatmentRepository, times(1)).findById(id);
    }

    @Test
    void getTypeOfTreatmentById_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(typeOfTreatmentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> typeOfTreatmentService.getTypeOfTreatmentById(id));

        verify(typeOfTreatmentRepository, times(1)).findById(id);
    }

    @Test
    void getAllTypesOfTreatment_WhenCalled_ReturnsTypeOfTreatmentList() {
        // Arrange
        TypeOfTreatment type1 = new TypeOfTreatment();
        type1.setId(1L);
        type1.setName("Type 1");

        TypeOfTreatment type2 = new TypeOfTreatment();
        type2.setId(2L);
        type2.setName("Type 2");

        List<TypeOfTreatment> expectedList = Arrays.asList(type1, type2);

        when(typeOfTreatmentRepository.findAll()).thenReturn(expectedList);

        // Act
        List<TypeOfTreatment> result = typeOfTreatmentService.getAllTypesOfTreatment();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Type 1", result.get(0).getName());
        assertEquals("Type 2", result.get(1).getName());

        verify(typeOfTreatmentRepository, times(1)).findAll();
    }

    @Test
    void createTypeOfTreatment_WhenValidType_ReturnsSavedType() {
        // Arrange
        Treatment treatment = new Treatment();
        treatment.setId(1L);

        TypeOfTreatment typeToSave = new TypeOfTreatment();
        typeToSave.setName("Immunotherapy");
        typeToSave.setTreatment(treatment);

        TypeOfTreatment savedType = new TypeOfTreatment();
        savedType.setId(1L);
        savedType.setName("Immunotherapy");
        savedType.setTreatment(treatment);

        when(typeOfTreatmentRepository.save(typeToSave)).thenReturn(savedType);

        // Act
        TypeOfTreatment result = typeOfTreatmentService.createTypeOfTreatment(typeToSave);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Immunotherapy", result.getName());
        assertEquals(treatment, result.getTreatment());

        verify(typeOfTreatmentRepository, times(1)).save(typeToSave);
    }

    @Test
    void updateTypeOfTreatment_WhenTypeExists_ReturnsUpdatedType() {
        // Arrange
        Long id = 1L;
        Treatment treatment = new Treatment();
        treatment.setId(1L);

        TypeOfTreatment existingType = new TypeOfTreatment();
        existingType.setId(id);
        existingType.setName("Old Type");
        existingType.setTreatment(treatment);

        TypeOfTreatment updatedType = new TypeOfTreatment();
        updatedType.setId(id);
        updatedType.setName("Updated Type");
        updatedType.setTreatment(treatment);

        when(typeOfTreatmentRepository.findById(id)).thenReturn(Optional.of(existingType));
        when(typeOfTreatmentRepository.save(existingType)).thenReturn(updatedType);

        // Act
        TypeOfTreatment result = typeOfTreatmentService.updateTypeOfTreatment(updatedType);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Updated Type", result.getName());

        verify(typeOfTreatmentRepository, times(1)).findById(id);
        verify(typeOfTreatmentRepository, times(1)).save(existingType);
    }

    @Test
    void deleteTypeOfTreatment_WhenIdExists_DeletesType() {
        // Arrange
        Long id = 1L;
        TypeOfTreatment type = new TypeOfTreatment();
        type.setId(id);

        when(typeOfTreatmentRepository.findById(id)).thenReturn(Optional.of(type));

        // Act
        typeOfTreatmentService.deleteTypeOfTreatment(id);

        // Assert
        verify(typeOfTreatmentRepository, times(1)).findById(id);
        verify(typeOfTreatmentRepository, times(1)).delete(type);
    }
}*/