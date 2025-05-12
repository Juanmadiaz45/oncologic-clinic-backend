package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.exception.runtime.appointment.MedicalAppointmentNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalAppointmentMapper;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.appointment.MedicalOfficeRepository;
import com.oncologic.clinic.repository.appointment.MedicalTaskRepository;
import com.oncologic.clinic.repository.appointment.TypeOfMedicalAppointmentRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.repository.personal.DoctorRepository;
import com.oncologic.clinic.service.appointment.impl.MedicalAppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentServiceTest {

    @Mock
    private MedicalAppointmentRepository medicalAppointmentRepository;

    @Mock
    private MedicalAppointmentMapper medicalAppointmentMapper;

    @InjectMocks
    private MedicalAppointmentServiceImpl medicalAppointmentService;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private TypeOfMedicalAppointmentRepository typeRepository;

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @Mock
    private MedicalTaskRepository medicalTaskRepository;

    @Mock
    private MedicalOfficeRepository medicalOfficeRepository;

    private MedicalAppointment medicalAppointment;
    private MedicalAppointmentResponseDTO medicalAppointmentResponseDTO;
    private MedicalAppointmentDTO medicalAppointmentDTO;

    @BeforeEach
    void setUp() {
        medicalAppointment = new MedicalAppointment();
        medicalAppointment.setId(1L);
        medicalAppointment.setAppointmentDate(LocalDateTime.now());

        medicalAppointmentResponseDTO = new MedicalAppointmentResponseDTO();
        medicalAppointmentResponseDTO.setId(1L);

        medicalAppointmentDTO = new MedicalAppointmentDTO();
        medicalAppointmentDTO.setDoctorId(1L);
        medicalAppointmentDTO.setTypeOfMedicalAppointmentId(1L);
        medicalAppointmentDTO.setMedicalHistoryId(1L);
    }

    @Test
    void getMedicalAppointmentById_whenIdExists_shouldReturnAppointmentDTO() {
        // Arrange
        when(medicalAppointmentRepository.findById(1L))
                .thenReturn(Optional.of(medicalAppointment));
        when(medicalAppointmentMapper.toDto(medicalAppointment))
                .thenReturn(medicalAppointmentResponseDTO);

        // Act
        MedicalAppointmentResponseDTO result = medicalAppointmentService.getMedicalAppointmentById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(medicalAppointmentRepository, times(1)).findById(1L);
        verify(medicalAppointmentMapper, times(1)).toDto(medicalAppointment);
    }

    @Test
    void getMedicalAppointmentById_whenIdDoesNotExist_shouldThrowException() {
        // Arrange
        when(medicalAppointmentRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalAppointmentNotFoundException.class, () -> {
            medicalAppointmentService.getMedicalAppointmentById(1L);
        });

        verify(medicalAppointmentRepository, times(1)).findById(1L);
    }

    @Test
    void getAllMedicalAppointments_whenAppointmentsExist_shouldReturnListOfDTOs() {
        // Arrange
        MedicalAppointment appointment2 = new MedicalAppointment();
        appointment2.setId(2L);

        MedicalAppointmentResponseDTO dto2 = new MedicalAppointmentResponseDTO();
        dto2.setId(2L);

        List<MedicalAppointment> appointments = Arrays.asList(medicalAppointment, appointment2);
        List<MedicalAppointmentResponseDTO> expectedDTOs = Arrays.asList(medicalAppointmentResponseDTO, dto2);

        when(medicalAppointmentRepository.findAll()).thenReturn(appointments);
        when(medicalAppointmentMapper.toDto(medicalAppointment)).thenReturn(medicalAppointmentResponseDTO);
        when(medicalAppointmentMapper.toDto(appointment2)).thenReturn(dto2);

        // Act
        List<MedicalAppointmentResponseDTO> result = medicalAppointmentService.getAllMedicalAppointments();

        // Assert
        assertEquals(2, result.size());
        assertEquals(expectedDTOs, result);
        verify(medicalAppointmentRepository, times(1)).findAll();
    }

    @Test
    void createMedicalAppointment_whenValidAppointment_shouldSaveAndReturnDTO() {
        // Arrange
        when(medicalAppointmentMapper.toEntity(medicalAppointmentDTO))
                .thenReturn(medicalAppointment);

        when(doctorRepository.findById(1L)).thenReturn(Optional.of(new Doctor()));
        when(typeRepository.findById(1L)).thenReturn(Optional.of(new TypeOfMedicalAppointment()));
        when(medicalHistoryRepository.findById(1L)).thenReturn(Optional.of(new MedicalHistory()));

        when(medicalAppointmentRepository.save(medicalAppointment))
                .thenReturn(medicalAppointment);
        when(medicalAppointmentMapper.toDto(medicalAppointment))
                .thenReturn(medicalAppointmentResponseDTO);

        // Act
        MedicalAppointmentResponseDTO result = medicalAppointmentService
                .createMedicalAppointment(medicalAppointmentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(medicalAppointmentResponseDTO, result);
        verify(medicalAppointmentRepository, times(2)).save(medicalAppointment); // Se llama 2 veces en el servicio
    }

    @Test
    void updateMedicalAppointment_whenIdExists_shouldUpdateAndReturnDTO() {
        // Arrange
        MedicalAppointment existingAppointment = new MedicalAppointment();
        existingAppointment.setId(1L);

        when(medicalAppointmentRepository.findById(1L))
                .thenReturn(Optional.of(existingAppointment));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(new Doctor()));
        when(typeRepository.findById(1L)).thenReturn(Optional.of(new TypeOfMedicalAppointment()));
        when(medicalAppointmentRepository.save(existingAppointment))
                .thenReturn(existingAppointment);
        when(medicalAppointmentMapper.toDto(existingAppointment))
                .thenReturn(medicalAppointmentResponseDTO);

        // Act
        MedicalAppointmentResponseDTO result = medicalAppointmentService
                .updateMedicalAppointment(1L, medicalAppointmentDTO);

        // Assert
        assertNotNull(result);
        verify(medicalAppointmentRepository, times(1)).findById(1L);
        verify(medicalAppointmentRepository, times(1)).save(existingAppointment);
    }

    @Test
    void updateMedicalAppointment_whenIdDoesNotExist_shouldThrowException() {
        // Arrange
        when(medicalAppointmentRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalAppointmentNotFoundException.class, () -> {
            medicalAppointmentService.updateMedicalAppointment(99L, medicalAppointmentDTO);
        });

        verify(medicalAppointmentRepository, times(1)).findById(99L);
        verify(medicalAppointmentRepository, never()).save(any());
    }

    @Test
    void deleteMedicalAppointment_whenIdExists_shouldDeleteAppointment() {
        // Arrange
        when(medicalAppointmentRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicalAppointmentRepository).deleteById(1L);

        // Act
        medicalAppointmentService.deleteMedicalAppointment(1L);

        // Assert
        verify(medicalAppointmentRepository, times(1)).existsById(1L);
        verify(medicalAppointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMedicalAppointment_whenIdDoesNotExist_shouldThrowException() {
        // Arrange
        when(medicalAppointmentRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(MedicalAppointmentNotFoundException.class, () -> {
            medicalAppointmentService.deleteMedicalAppointment(1L);
        });

        verify(medicalAppointmentRepository, times(1)).existsById(1L);
        verify(medicalAppointmentRepository, never()).deleteById(any());
    }
}