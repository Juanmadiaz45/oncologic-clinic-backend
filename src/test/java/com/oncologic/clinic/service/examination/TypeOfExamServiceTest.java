package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.repository.examination.TypeOfExamRepository;
import com.oncologic.clinic.service.examination.impl.TypeOfExamServiceImpl;
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
public class TypeOfExamServiceTest {

    @Mock
    private TypeOfExamRepository typeOfExamRepository;

    @InjectMocks
    private TypeOfExamServiceImpl typeOfExamService;

    @Test
    void getTypeOfExamById_WhenTypeExists_ReturnsTypeOfExam() {
        // Arrange
        Long id = 1L;
        TypeOfExam mockType = new TypeOfExam();
        mockType.setId(id);
        when(typeOfExamRepository.findById(id)).thenReturn(Optional.of(mockType));

        // Act
        TypeOfExam result = typeOfExamService.getTypeOfExamById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(typeOfExamRepository, times(1)).findById(id);
    }

    @Test
    void getTypeOfExamById_WhenTypeDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(typeOfExamRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> typeOfExamService.getTypeOfExamById(id));
        verify(typeOfExamRepository, times(1)).findById(id);
    }

    @Test
    void getAllTypesOfExam_WhenTypesExist_ReturnsTypeOfExamList() {
        // Arrange
        TypeOfExam type1 = new TypeOfExam();
        type1.setId(1L);
        TypeOfExam type2 = new TypeOfExam();
        type2.setId(2L);
        List<TypeOfExam> mockTypes = Arrays.asList(type1, type2);
        when(typeOfExamRepository.findAll()).thenReturn(mockTypes);

        // Act
        List<TypeOfExam> results = typeOfExamService.getAllTypesOfExam();

        // Assert
        assertEquals(2, results.size());
        verify(typeOfExamRepository, times(1)).findAll();
    }

    @Test
    void getAllTypesOfExam_WhenNoTypesExist_ReturnsEmptyList() {
        // Arrange
        when(typeOfExamRepository.findAll()).thenReturn(List.of());

        // Act
        List<TypeOfExam> results = typeOfExamService.getAllTypesOfExam();

        // Assert
        assertTrue(results.isEmpty());
        verify(typeOfExamRepository, times(1)).findAll();
    }

    @Test
    void createTypeOfExam_WhenValidType_ReturnsSavedType() {
        // Arrange
        TypeOfExam newType = new TypeOfExam();
        newType.setName("Blood Test");
        newType.setDescription("Standard blood test");

        TypeOfExam savedType = new TypeOfExam();
        savedType.setId(1L);
        savedType.setName(newType.getName());
        savedType.setDescription(newType.getDescription());

        when(typeOfExamRepository.save(newType)).thenReturn(savedType);

        // Act
        TypeOfExam result = typeOfExamService.createTypeOfExam(newType);

        // Assert
        assertNotNull(result.getId());
        assertEquals("Blood Test", result.getName());
        verify(typeOfExamRepository, times(1)).save(newType);
    }

    @Test
    void createTypeOfExam_WhenNullTypeProvided_ThrowsIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> typeOfExamService.createTypeOfExam(null));
        verify(typeOfExamRepository, never()).save(any());
    }

    @Test
    void updateTypeOfExam_WhenTypeExists_ReturnsUpdatedType() {
        // Arrange
        Long id = 1L;
        TypeOfExam existingType = new TypeOfExam();
        existingType.setId(id);
        existingType.setName("Old Name");

        TypeOfExam updatedType = new TypeOfExam();
        updatedType.setId(id);
        updatedType.setName("New Name");
        updatedType.setDescription("Updated description");

        when(typeOfExamRepository.findById(id)).thenReturn(Optional.of(existingType));
        when(typeOfExamRepository.save(existingType)).thenReturn(updatedType);

        // Act
        TypeOfExam result = typeOfExamService.updateTypeOfExam(updatedType);

        // Assert
        assertEquals("New Name", result.getName());
        assertEquals("Updated description", result.getDescription());
        verify(typeOfExamRepository, times(1)).findById(id);
        verify(typeOfExamRepository, times(1)).save(existingType);
    }

    @Test
    void updateTypeOfExam_WhenTypeDoesNotExist_ThrowsException() {
        // Arrange
        TypeOfExam nonExistentType = new TypeOfExam();
        nonExistentType.setId(99L);
        when(typeOfExamRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> typeOfExamService.updateTypeOfExam(nonExistentType));
        verify(typeOfExamRepository, times(1)).findById(99L);
        verify(typeOfExamRepository, never()).save(any());
    }

    @Test
    void deleteTypeOfExam_WhenTypeExists_DeletesType() {
        // Arrange
        Long id = 1L;
        TypeOfExam type = new TypeOfExam();
        type.setId(id);
        when(typeOfExamRepository.findById(id)).thenReturn(Optional.of(type));

        // Act
        typeOfExamService.deleteTypeOfExam(id);

        // Assert
        verify(typeOfExamRepository, times(1)).findById(id);
        verify(typeOfExamRepository, times(1)).delete(type);
    }

    @Test
    void deleteTypeOfExam_WhenTypeDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(typeOfExamRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> typeOfExamService.deleteTypeOfExam(id));
        verify(typeOfExamRepository, times(1)).findById(id);
        verify(typeOfExamRepository, never()).delete(any());
    }
}