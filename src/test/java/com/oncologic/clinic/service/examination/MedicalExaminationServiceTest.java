package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.request.MedicalExaminationRequestDTO;
import com.oncologic.clinic.dto.examination.response.MedicalExaminationResponseDTO;
import com.oncologic.clinic.dto.examination.update.MedicalExaminationUpdateDTO;
import com.oncologic.clinic.entity.examination.Laboratory;
import com.oncologic.clinic.entity.examination.MedicalExamination;
import com.oncologic.clinic.entity.examination.TypeOfExam;
import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.exception.runtime.examination.MedicalExaminationNotFoundException;
import com.oncologic.clinic.mapper.examination.MedicalExaminationMapper;
import com.oncologic.clinic.repository.examination.LaboratoryRepository;
import com.oncologic.clinic.repository.examination.MedicalExaminationRepository;
import com.oncologic.clinic.repository.examination.TypeOfExamRepository;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.examination.impl.MedicalExaminationServiceImpl;
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
public class MedicalExaminationServiceTest {

    @Mock
    private MedicalExaminationRepository medicalExaminationRepository;

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @Mock
    private TypeOfExamRepository typeOfExamRepository;

    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @Mock
    private MedicalExaminationMapper medicalExaminationMapper;

    @InjectMocks
    private MedicalExaminationServiceImpl medicalExaminationService;

    private MedicalExamination medicalExamination;
    private MedicalExaminationResponseDTO responseDTO;
    private MedicalExaminationRequestDTO requestDTO;
    private MedicalExaminationUpdateDTO updateDTO;
    private Laboratory laboratory;
    private TypeOfExam typeOfExam;
    private MedicalHistory medicalHistory;

    @BeforeEach
    void setUp() {
        medicalExamination = new MedicalExamination();
        medicalExamination.setId("EXAM-123");
        medicalExamination.setDateOfRealization(LocalDateTime.now());

        laboratory = new Laboratory();
        laboratory.setId(1L);

        typeOfExam = new TypeOfExam();
        typeOfExam.setId(1L);

        medicalHistory = new MedicalHistory();
        medicalHistory.setId(1L);

        medicalExamination.setLaboratory(laboratory);
        medicalExamination.setTypeOfExam(typeOfExam);
        medicalExamination.setMedicalHistory(medicalHistory);

        responseDTO = new MedicalExaminationResponseDTO();
        responseDTO.setId("EXAM-123");
        responseDTO.setDateOfRealization(medicalExamination.getDateOfRealization());

        requestDTO = new MedicalExaminationRequestDTO();
        requestDTO.setLaboratoryId(1L);
        requestDTO.setTypeOfExamId(1L);
        requestDTO.setMedicalHistoryId(1L);

        updateDTO = new MedicalExaminationUpdateDTO();
        updateDTO.setLaboratoryId(1L);
        updateDTO.setTypeOfExamId(1L);
    }

    @Test
    void getMedicalExaminationById_WhenExamExists_ReturnsMedicalExaminationDTO() {
        // Arrange
        when(medicalExaminationRepository.findById("EXAM-123")).thenReturn(Optional.of(medicalExamination));
        when(medicalExaminationMapper.toDto(medicalExamination)).thenReturn(responseDTO);

        // Act
        MedicalExaminationResponseDTO result = medicalExaminationService.getMedicalExaminationById("EXAM-123");

        // Assert
        assertNotNull(result);
        assertEquals("EXAM-123", result.getId());
        verify(medicalExaminationRepository, times(1)).findById("EXAM-123");
        verify(medicalExaminationMapper, times(1)).toDto(medicalExamination);
    }

    @Test
    void getMedicalExaminationById_WhenExamDoesNotExist_ThrowsException() {
        // Arrange
        when(medicalExaminationRepository.findById("NON-EXIST")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MedicalExaminationNotFoundException.class, () ->
                medicalExaminationService.getMedicalExaminationById("NON-EXIST"));
        verify(medicalExaminationRepository, times(1)).findById("NON-EXIST");
    }

    @Test
    void getAllMedicalExaminations_WhenExamsExist_ReturnsMedicalExaminationDTOList() {
        // Arrange
        MedicalExamination exam2 = new MedicalExamination();
        exam2.setId("EXAM-456");

        MedicalExaminationResponseDTO responseDTO2 = new MedicalExaminationResponseDTO();
        responseDTO2.setId("EXAM-456");

        when(medicalExaminationRepository.findAll()).thenReturn(Arrays.asList(medicalExamination, exam2));
        when(medicalExaminationMapper.toDto(medicalExamination)).thenReturn(responseDTO);
        when(medicalExaminationMapper.toDto(exam2)).thenReturn(responseDTO2);

        // Act
        List<MedicalExaminationResponseDTO> results = medicalExaminationService.getAllMedicalExaminations();

        // Assert
        assertEquals(2, results.size());
        verify(medicalExaminationRepository, times(1)).findAll();
        verify(medicalExaminationMapper, times(1)).toDto(medicalExamination);
        verify(medicalExaminationMapper, times(1)).toDto(exam2);
    }

    @Test
    void createMedicalExamination_WhenValidRequest_ReturnsSavedMedicalExaminationDTO() {
        // Arrange
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratory));
        when(typeOfExamRepository.findById(1L)).thenReturn(Optional.of(typeOfExam));
        when(medicalHistoryRepository.findById(1L)).thenReturn(Optional.of(medicalHistory));
        when(medicalExaminationMapper.toEntity(requestDTO)).thenReturn(medicalExamination);
        when(medicalExaminationRepository.save(medicalExamination)).thenReturn(medicalExamination);
        when(medicalExaminationMapper.toDto(medicalExamination)).thenReturn(responseDTO);

        // Act
        MedicalExaminationResponseDTO result = medicalExaminationService.createMedicalExamination(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("EXAM-123", result.getId());
        verify(medicalExaminationRepository, times(1)).save(medicalExamination);
    }

    @Test
    void updateMedicalExamination_WhenExamExists_ReturnsUpdatedMedicalExaminationDTO() {
        // Arrange
        MedicalExamination updatedExam = new MedicalExamination();
        updatedExam.setId("EXAM-123");
        updatedExam.setDateOfRealization(LocalDateTime.now().plusDays(1));

        MedicalExaminationResponseDTO updatedResponse = new MedicalExaminationResponseDTO();
        updatedResponse.setId("EXAM-123");
        updatedResponse.setDateOfRealization(updatedExam.getDateOfRealization());

        when(medicalExaminationRepository.findById("EXAM-123")).thenReturn(Optional.of(medicalExamination));
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratory));
        when(typeOfExamRepository.findById(1L)).thenReturn(Optional.of(typeOfExam));
        when(medicalExaminationRepository.save(any(MedicalExamination.class))).thenReturn(updatedExam);
        when(medicalExaminationMapper.toDto(updatedExam)).thenReturn(updatedResponse);

        // Act
        MedicalExaminationResponseDTO result = medicalExaminationService.updateMedicalExamination("EXAM-123", updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(updatedExam.getDateOfRealization(), result.getDateOfRealization());
        verify(medicalExaminationRepository, times(1)).findById("EXAM-123");
        verify(medicalExaminationRepository, times(1)).save(any(MedicalExamination.class));
    }

    @Test
    void deleteMedicalExamination_WhenExamExists_DeletesExam() {
        // Arrange
        when(medicalExaminationRepository.existsById("EXAM-123")).thenReturn(true);
        doNothing().when(medicalExaminationRepository).deleteById("EXAM-123");

        // Act
        medicalExaminationService.deleteMedicalExamination("EXAM-123");

        // Assert
        verify(medicalExaminationRepository, times(1)).existsById("EXAM-123");
        verify(medicalExaminationRepository, times(1)).deleteById("EXAM-123");
    }

    @Test
    void deleteMedicalExamination_WhenExamDoesNotExist_ThrowsException() {
        // Arrange
        when(medicalExaminationRepository.existsById("NON-EXIST")).thenReturn(false);

        // Act & Assert
        assertThrows(MedicalExaminationNotFoundException.class, () ->
                medicalExaminationService.deleteMedicalExamination("NON-EXIST"));
        verify(medicalExaminationRepository, times(1)).existsById("NON-EXIST");
        verify(medicalExaminationRepository, never()).deleteById(any());
    }
}