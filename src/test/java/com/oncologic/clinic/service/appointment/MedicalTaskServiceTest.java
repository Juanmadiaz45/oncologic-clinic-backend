package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.dto.appointment.MedicalTaskDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalTask;
import com.oncologic.clinic.exception.runtime.appointment.MedicalTaskNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalTaskMapper;
import com.oncologic.clinic.repository.appointment.MedicalTaskRepository;
import com.oncologic.clinic.service.appointment.impl.MedicalTaskServiceImpl;
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
class MedicalTaskServiceTest {

    @Mock
    private MedicalTaskRepository medicalTaskRepository;

    @Mock
    private MedicalTaskMapper medicalTaskMapper;

    @InjectMocks
    private MedicalTaskServiceImpl medicalTaskService;

    private MedicalTask medicalTask;
    private MedicalTaskResponseDTO medicalTaskResponseDTO;
    private MedicalTaskDTO medicalTaskDTO;

    @BeforeEach
    void setUp() {
        medicalTask = new MedicalTask();
        medicalTask.setId(1L);
        medicalTask.setDescription("Análisis de sangre");
        medicalTask.setEstimatedTime(30L);
        medicalTask.setStatus("Pendiente");
        medicalTask.setResponsible("Laboratorio Central");

        medicalTaskResponseDTO = new MedicalTaskResponseDTO();
        medicalTaskResponseDTO.setId(1L);
        medicalTaskResponseDTO.setDescription("Análisis de sangre");
        medicalTaskResponseDTO.setEstimatedTime(30L);
        medicalTaskResponseDTO.setStatus("Pendiente");
        medicalTaskResponseDTO.setResponsible("Laboratorio Central");

        medicalTaskDTO = new MedicalTaskDTO();
        medicalTaskDTO.setDescription("Análisis de sangre");
        medicalTaskDTO.setEstimatedTime(30L);
        medicalTaskDTO.setStatus("Pendiente");
        medicalTaskDTO.setResponsible("Laboratorio Central");
    }

    @Test
    void getMedicalTaskById_WhenIdExists_ShouldReturnMedicalTaskDTO() {
        // Arrange
        when(medicalTaskRepository.findById(1L)).thenReturn(Optional.of(medicalTask));
        when(medicalTaskMapper.toDto(medicalTask)).thenReturn(medicalTaskResponseDTO);

        // Act
        MedicalTaskResponseDTO result = medicalTaskService.getMedicalTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Análisis de sangre", result.getDescription());
        verify(medicalTaskRepository, times(1)).findById(1L);
        verify(medicalTaskMapper, times(1)).toDto(medicalTask);
    }

    @Test
    void getMedicalTaskById_WhenIdNotExists_ShouldThrowException() {
        // Arrange
        when(medicalTaskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalTaskNotFoundException.class, () -> {
            medicalTaskService.getMedicalTaskById(1L);
        });

        verify(medicalTaskRepository, times(1)).findById(1L);
    }

    @Test
    void getAllMedicalTasks_WhenTasksExist_ShouldReturnTaskDTOList() {
        // Arrange
        MedicalTask task2 = new MedicalTask();
        task2.setId(2L);
        task2.setDescription("Radiografía");

        MedicalTaskResponseDTO dto2 = new MedicalTaskResponseDTO();
        dto2.setId(2L);
        dto2.setDescription("Radiografía");

        when(medicalTaskRepository.findAll()).thenReturn(Arrays.asList(medicalTask, task2));
        when(medicalTaskMapper.toDto(medicalTask)).thenReturn(medicalTaskResponseDTO);
        when(medicalTaskMapper.toDto(task2)).thenReturn(dto2);

        // Act
        List<MedicalTaskResponseDTO> result = medicalTaskService.getAllMedicalTasks();

        // Assert
        assertEquals(2, result.size());
        verify(medicalTaskRepository, times(1)).findAll();
        verify(medicalTaskMapper, times(1)).toDto(medicalTask);
        verify(medicalTaskMapper, times(1)).toDto(task2);
    }

    @Test
    void createMedicalTask_WhenValidTask_ShouldReturnSavedTaskDTO() {
        // Arrange
        when(medicalTaskMapper.toEntity(medicalTaskDTO)).thenReturn(medicalTask);
        when(medicalTaskRepository.save(medicalTask)).thenReturn(medicalTask);
        when(medicalTaskMapper.toDto(medicalTask)).thenReturn(medicalTaskResponseDTO);

        // Act
        MedicalTaskResponseDTO result = medicalTaskService.createMedicalTask(medicalTaskDTO);

        // Assert
        assertNotNull(result);
        assertEquals(medicalTaskResponseDTO, result);
        verify(medicalTaskRepository, times(1)).save(medicalTask);
        verify(medicalTaskMapper, times(1)).toEntity(medicalTaskDTO);
        verify(medicalTaskMapper, times(1)).toDto(medicalTask);
    }

    @Test
    void updateMedicalTask_WhenTaskExists_ShouldReturnUpdatedTaskDTO() {
        // Arrange
        MedicalTask updatedTask = new MedicalTask();
        updatedTask.setId(1L);
        updatedTask.setDescription("Análisis de sangre completo");
        updatedTask.setStatus("Completado");

        MedicalTaskResponseDTO updatedDTO = new MedicalTaskResponseDTO();
        updatedDTO.setId(1L);
        updatedDTO.setDescription("Análisis de sangre completo");
        updatedDTO.setStatus("Completado");

        when(medicalTaskRepository.findById(1L)).thenReturn(Optional.of(medicalTask));
        when(medicalTaskRepository.save(any(MedicalTask.class))).thenReturn(updatedTask);
        when(medicalTaskMapper.toDto(updatedTask)).thenReturn(updatedDTO);

        // Act
        MedicalTaskResponseDTO result = medicalTaskService.updateMedicalTask(1L, medicalTaskDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Completado", result.getStatus());
        verify(medicalTaskRepository, times(1)).findById(1L);
        verify(medicalTaskRepository, times(1)).save(any(MedicalTask.class));
    }

    @Test
    void updateMedicalTask_WhenTaskNotExists_ShouldThrowException() {
        // Arrange
        when(medicalTaskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalTaskNotFoundException.class, () -> {
            medicalTaskService.updateMedicalTask(99L, medicalTaskDTO);
        });

        verify(medicalTaskRepository, times(1)).findById(99L);
        verify(medicalTaskRepository, never()).save(any());
    }

    @Test
    void deleteMedicalTask_WhenTaskExists_ShouldDeleteTask() {
        // Arrange
        when(medicalTaskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicalTaskRepository).deleteById(1L);

        // Act
        medicalTaskService.deleteMedicalTask(1L);

        // Assert
        verify(medicalTaskRepository, times(1)).existsById(1L);
        verify(medicalTaskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedicalTask_WhenTaskNotExists_ShouldThrowException() {
        // Arrange
        when(medicalTaskRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(MedicalTaskNotFoundException.class, () -> {
            medicalTaskService.deleteMedicalTask(1L);
        });

        verify(medicalTaskRepository, times(1)).existsById(1L);
        verify(medicalTaskRepository, never()).deleteById(any());
    }
}
