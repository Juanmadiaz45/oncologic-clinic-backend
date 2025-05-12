package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.TypeOfExamDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.exception.runtime.examination.TypeOfExamNotFoundException;
import com.oncologic.clinic.mapper.examination.TypeOfExamMapper;
import com.oncologic.clinic.repository.examination.TypeOfExamRepository;
import com.oncologic.clinic.service.examination.impl.TypeOfExamServiceImpl;
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
public class TypeOfExamServiceTest {

    @Mock
    private TypeOfExamRepository typeOfExamRepository;

    @Mock
    private TypeOfExamMapper typeOfExamMapper;

    @InjectMocks
    private TypeOfExamServiceImpl typeOfExamService;

    private TypeOfExam typeOfExam;
    private TypeOfExamResponseDTO typeOfExamResponseDTO;
    private TypeOfExamDTO typeOfExamDTO;

    @BeforeEach
    void setUp() {
        typeOfExam = new TypeOfExam();
        typeOfExam.setId(1L);
        typeOfExam.setName("Blood Test");
        typeOfExam.setDescription("Standard blood test");

        typeOfExamResponseDTO = new TypeOfExamResponseDTO();
        typeOfExamResponseDTO.setId(1L);
        typeOfExamResponseDTO.setName("Blood Test");
        typeOfExamResponseDTO.setDescription("Standard blood test");

        typeOfExamDTO = new TypeOfExamDTO();
        typeOfExamDTO.setName("Blood Test");
        typeOfExamDTO.setDescription("Standard blood test");
    }

    @Test
    void getTypeOfExamById_WhenTypeExists_ReturnsTypeOfExamDTO() {
        // Arrange
        when(typeOfExamRepository.findById(1L)).thenReturn(Optional.of(typeOfExam));
        when(typeOfExamMapper.toDto(typeOfExam)).thenReturn(typeOfExamResponseDTO);

        // Act
        TypeOfExamResponseDTO result = typeOfExamService.getTypeOfExamById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Blood Test", result.getName());
        verify(typeOfExamRepository, times(1)).findById(1L);
        verify(typeOfExamMapper, times(1)).toDto(typeOfExam);
    }

    @Test
    void getTypeOfExamById_WhenTypeDoesNotExist_ThrowsException() {
        // Arrange
        when(typeOfExamRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TypeOfExamNotFoundException.class, () ->
                typeOfExamService.getTypeOfExamById(99L));
        verify(typeOfExamRepository, times(1)).findById(99L);
    }

    @Test
    void getAllTypesOfExam_WhenTypesExist_ReturnsTypeOfExamDTOList() {
        // Arrange
        TypeOfExam type2 = new TypeOfExam();
        type2.setId(2L);
        type2.setName("X-Ray");

        TypeOfExamResponseDTO responseDTO2 = new TypeOfExamResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setName("X-Ray");

        when(typeOfExamRepository.findAll()).thenReturn(Arrays.asList(typeOfExam, type2));
        when(typeOfExamMapper.toDto(typeOfExam)).thenReturn(typeOfExamResponseDTO);
        when(typeOfExamMapper.toDto(type2)).thenReturn(responseDTO2);

        // Act
        List<TypeOfExamResponseDTO> results = typeOfExamService.getAllTypesOfExam();

        // Assert
        assertEquals(2, results.size());
        verify(typeOfExamRepository, times(1)).findAll();
        verify(typeOfExamMapper, times(1)).toDto(typeOfExam);
        verify(typeOfExamMapper, times(1)).toDto(type2);
    }

    @Test
    void createTypeOfExam_WhenValidRequest_ReturnsSavedTypeOfExamDTO() {
        // Arrange
        when(typeOfExamMapper.toEntity(typeOfExamDTO)).thenReturn(typeOfExam);
        when(typeOfExamRepository.save(typeOfExam)).thenReturn(typeOfExam);
        when(typeOfExamMapper.toDto(typeOfExam)).thenReturn(typeOfExamResponseDTO);

        // Act
        TypeOfExamResponseDTO result = typeOfExamService.createTypeOfExam(typeOfExamDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Blood Test", result.getName());
        verify(typeOfExamRepository, times(1)).save(typeOfExam);
        verify(typeOfExamMapper, times(1)).toEntity(typeOfExamDTO);
        verify(typeOfExamMapper, times(1)).toDto(typeOfExam);
    }

    @Test
    void updateTypeOfExam_WhenTypeExists_ReturnsUpdatedTypeOfExamDTO() {
        // Arrange
        TypeOfExam updatedType = new TypeOfExam();
        updatedType.setId(1L);
        updatedType.setName("Updated Blood Test");

        TypeOfExamResponseDTO updatedResponse = new TypeOfExamResponseDTO();
        updatedResponse.setId(1L);
        updatedResponse.setName("Updated Blood Test");

        when(typeOfExamRepository.findById(1L)).thenReturn(Optional.of(typeOfExam));
        when(typeOfExamRepository.save(any(TypeOfExam.class))).thenReturn(updatedType);
        when(typeOfExamMapper.toDto(updatedType)).thenReturn(updatedResponse);

        // Act
        TypeOfExamResponseDTO result = typeOfExamService.updateTypeOfExam(1L, typeOfExamDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Blood Test", result.getName());
        verify(typeOfExamRepository, times(1)).findById(1L);
        verify(typeOfExamRepository, times(1)).save(any(TypeOfExam.class));
    }

    @Test
    void updateTypeOfExam_WhenTypeDoesNotExist_ThrowsException() {
        // Arrange
        when(typeOfExamRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TypeOfExamNotFoundException.class, () ->
                typeOfExamService.updateTypeOfExam(99L, typeOfExamDTO));
        verify(typeOfExamRepository, times(1)).findById(99L);
        verify(typeOfExamRepository, never()).save(any());
    }

    @Test
    void deleteTypeOfExam_WhenTypeExists_DeletesType() {
        // Arrange
        when(typeOfExamRepository.existsById(1L)).thenReturn(true);
        doNothing().when(typeOfExamRepository).deleteById(1L);

        // Act
        typeOfExamService.deleteTypeOfExam(1L);

        // Assert
        verify(typeOfExamRepository, times(1)).existsById(1L);
        verify(typeOfExamRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTypeOfExam_WhenTypeDoesNotExist_ThrowsException() {
        // Arrange
        when(typeOfExamRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(TypeOfExamNotFoundException.class, () ->
                typeOfExamService.deleteTypeOfExam(99L));
        verify(typeOfExamRepository, times(1)).existsById(99L);
        verify(typeOfExamRepository, never()).deleteById(any());
    }
}