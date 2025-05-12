package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.dto.appointment.TypeOfMedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.TypeOfMedicalAppointmentResponseDTO;
import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import com.oncologic.clinic.exception.runtime.appointment.TypeOfMedicalAppointmentNotFoundException;
import com.oncologic.clinic.mapper.appointment.TypeOfMedicalAppointmentMapper;
import com.oncologic.clinic.repository.appointment.TypeOfMedicalAppointmentRepository;
import com.oncologic.clinic.service.appointment.impl.TypeOfMedicalAppointmentServiceImpl;
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
class TypeOfMedicalAppointmentServiceTest {

    @Mock
    private TypeOfMedicalAppointmentRepository repository;

    @Mock
    private TypeOfMedicalAppointmentMapper mapper;

    @InjectMocks
    private TypeOfMedicalAppointmentServiceImpl service;

    private TypeOfMedicalAppointment type;
    private TypeOfMedicalAppointmentResponseDTO typeResponseDTO;
    private TypeOfMedicalAppointmentDTO typeDTO;

    @BeforeEach
    void setUp() {
        type = new TypeOfMedicalAppointment(1L, "Oncología", List.of());

        typeResponseDTO = new TypeOfMedicalAppointmentResponseDTO();
        typeResponseDTO.setId(1L);
        typeResponseDTO.setName("Oncología");

        typeDTO = new TypeOfMedicalAppointmentDTO();
        typeDTO.setName("Oncología");
    }

    @Test
    void getTypeOfMedicalAppointmentById_WhenIdExists_ReturnsTypeDTO() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(type));
        when(mapper.toDto(type)).thenReturn(typeResponseDTO);

        // Act
        TypeOfMedicalAppointmentResponseDTO result = service.getTypeOfMedicalAppointmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Oncología", result.getName());
        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).toDto(type);
    }

    @Test
    void getTypeOfMedicalAppointmentById_WhenIdNotExists_ThrowsException() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TypeOfMedicalAppointmentNotFoundException.class, () -> {
            service.getTypeOfMedicalAppointmentById(1L);
        });

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void getAllTypesOfMedicalAppointment_WhenCalled_ReturnsListOfTypeDTOs() {
        // Arrange
        TypeOfMedicalAppointment type2 = new TypeOfMedicalAppointment(2L, "Dermatología", List.of());
        TypeOfMedicalAppointmentResponseDTO dto2 = new TypeOfMedicalAppointmentResponseDTO();
        dto2.setId(2L);
        dto2.setName("Dermatología");

        when(repository.findAll()).thenReturn(Arrays.asList(type, type2));
        when(mapper.toDto(type)).thenReturn(typeResponseDTO);
        when(mapper.toDto(type2)).thenReturn(dto2);

        // Act
        List<TypeOfMedicalAppointmentResponseDTO> result = service.getAllTypesOfMedicalAppointment();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Oncología", result.get(0).getName());
        assertEquals("Dermatología", result.get(1).getName());
        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDto(type);
        verify(mapper, times(1)).toDto(type2);
    }

    @Test
    void createTypeOfMedicalAppointment_WhenValidInput_ReturnsCreatedTypeDTO() {
        // Arrange
        when(mapper.toEntity(typeDTO)).thenReturn(type);
        when(repository.save(type)).thenReturn(type);
        when(mapper.toDto(type)).thenReturn(typeResponseDTO);

        // Act
        TypeOfMedicalAppointmentResponseDTO result = service.createTypeOfMedicalAppointment(typeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Oncología", result.getName());
        verify(repository, times(1)).save(type);
        verify(mapper, times(1)).toEntity(typeDTO);
        verify(mapper, times(1)).toDto(type);
    }

    @Test
    void updateTypeOfMedicalAppointment_WhenValidInput_ReturnsUpdatedTypeDTO() {
        // Arrange
        TypeOfMedicalAppointment updatedType = new TypeOfMedicalAppointment(1L, "Ginecología", List.of());
        TypeOfMedicalAppointmentResponseDTO updatedDTO = new TypeOfMedicalAppointmentResponseDTO();
        updatedDTO.setId(1L);
        updatedDTO.setName("Ginecología");

        when(repository.findById(1L)).thenReturn(Optional.of(type));
        when(repository.save(any(TypeOfMedicalAppointment.class))).thenReturn(updatedType);
        when(mapper.toDto(updatedType)).thenReturn(updatedDTO);

        // Act
        TypeOfMedicalAppointmentResponseDTO result = service.updateTypeOfMedicalAppointment(1L, typeDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ginecología", result.getName());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(TypeOfMedicalAppointment.class));
    }

    @Test
    void updateTypeOfMedicalAppointment_WhenIdNotExists_ThrowsException() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TypeOfMedicalAppointmentNotFoundException.class, () -> {
            service.updateTypeOfMedicalAppointment(99L, typeDTO);
        });

        verify(repository, times(1)).findById(99L);
        verify(repository, never()).save(any());
    }

    @Test
    void deleteTypeOfMedicalAppointment_WhenIdExists_DeletesSuccessfully() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // Act
        service.deleteTypeOfMedicalAppointment(1L);

        // Assert
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTypeOfMedicalAppointment_WhenIdNotExists_ThrowsException() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(TypeOfMedicalAppointmentNotFoundException.class, () -> {
            service.deleteTypeOfMedicalAppointment(1L);
        });

        verify(repository, times(1)).existsById(1L);
        verify(repository, never()).deleteById(any());
    }
}