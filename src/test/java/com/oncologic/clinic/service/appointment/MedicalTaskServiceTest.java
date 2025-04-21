package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.entity.appointment.MedicalTask;
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

    @InjectMocks
    private MedicalTaskServiceImpl medicalTaskService;

    private MedicalTask medicalTask;

    @BeforeEach
    void setUp() {
        medicalTask = new MedicalTask();
        medicalTask.setId(1L);
        medicalTask.setDescription("Análisis de sangre");
        medicalTask.setEstimatedTime(30L);
        medicalTask.setStatus("Pendiente");
        medicalTask.setResponsible("Laboratorio Central");
    }

    @Test
    void getMedicalTaskById_WhenIdExists_ShouldReturnMedicalTask() {
        // Arrange
        when(medicalTaskRepository.findById(1L)).thenReturn(Optional.of(medicalTask));

        // Act
        MedicalTask result = medicalTaskService.getMedicalTaskById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Análisis de sangre", result.getDescription());
        verify(medicalTaskRepository, times(1)).findById(1L);
    }

    @Test
    void getMedicalTaskById_WhenIdNotExists_ShouldReturnNull() {
        // Arrange
        when(medicalTaskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        MedicalTask result = medicalTaskService.getMedicalTaskById(1L);

        // Assert
        assertNull(result);
        verify(medicalTaskRepository, times(1)).findById(1L);
    }

    @Test
    void getAllMedicalTasks_WhenTasksExist_ShouldReturnTaskList() {
        // Arrange
        MedicalTask task2 = new MedicalTask();
        task2.setId(2L);
        task2.setDescription("Radiografía");

        when(medicalTaskRepository.findAll()).thenReturn(Arrays.asList(medicalTask, task2));

        // Act
        List<MedicalTask> result = medicalTaskService.getAllMedicalTasks();

        // Assert
        assertEquals(2, result.size());
        verify(medicalTaskRepository, times(1)).findAll();
    }

    @Test
    void createMedicalTask_WhenValidTask_ShouldReturnSavedTask() {
        // Arrange
        when(medicalTaskRepository.save(medicalTask)).thenReturn(medicalTask);

        // Act
        MedicalTask result = medicalTaskService.createMedicalTask(medicalTask);

        // Assert
        assertNotNull(result);
        assertEquals(medicalTask, result);
        verify(medicalTaskRepository, times(1)).save(medicalTask);
    }

    @Test
    void updateMedicalTask_WhenTaskExists_ShouldReturnUpdatedTask() {
        // Arrange
        MedicalTask updatedTask = new MedicalTask();
        updatedTask.setId(1L);
        updatedTask.setDescription("Análisis de sangre completo");
        updatedTask.setStatus("Completado");

        when(medicalTaskRepository.existsById(1L)).thenReturn(true);
        when(medicalTaskRepository.save(updatedTask)).thenReturn(updatedTask);

        // Act
        MedicalTask result = medicalTaskService.updateMedicalTask(updatedTask);

        // Assert
        assertNotNull(result);
        assertEquals("Completado", result.getStatus());
        verify(medicalTaskRepository, times(1)).existsById(1L);
        verify(medicalTaskRepository, times(1)).save(updatedTask);
    }

    @Test
    void updateMedicalTask_WhenTaskNotExists_ShouldReturnNull() {
        // Arrange
        MedicalTask nonExistingTask = new MedicalTask();
        nonExistingTask.setId(99L);

        when(medicalTaskRepository.existsById(99L)).thenReturn(false);

        // Act
        MedicalTask result = medicalTaskService.updateMedicalTask(nonExistingTask);

        // Assert
        assertNull(result);
        verify(medicalTaskRepository, times(1)).existsById(99L);
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
    void deleteMedicalTask_WhenTaskNotExists_ShouldDoNothing() {
        // Arrange
        when(medicalTaskRepository.existsById(1L)).thenReturn(false);

        // Act
        medicalTaskService.deleteMedicalTask(1L);

        // Assert
        verify(medicalTaskRepository, times(1)).existsById(1L);
        verify(medicalTaskRepository, never()).deleteById(any());
    }
}