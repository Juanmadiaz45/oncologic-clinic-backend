package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.MedicalHistory;
import com.oncologic.clinic.entity.patient.Patient;
import com.oncologic.clinic.repository.patient.MedicalHistoryRepository;
import com.oncologic.clinic.service.patient.impl.MedicalHistoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MedicalHistoryServiceTest {
    @Mock
    private MedicalHistoryRepository medicalHistoryRepository;

    @InjectMocks
    private MedicalHistoryServiceImpl medicalHistoryService;

    private Patient mockPatient;

    @BeforeEach
    void setUp() {
        mockPatient = new Patient();
        mockPatient.setId(1L);
        mockPatient.setName("Pedro Paciente");
    }

    @Test
    void registerMedicalHistory_WithHealthStatus_ShouldSaveCorrectly() {
        // Arrange
        String healthStatus = "Estable";
        MedicalHistory savedHistory = new MedicalHistory();
        savedHistory.setId(10L);

        when(medicalHistoryRepository.save(any(MedicalHistory.class))).thenReturn(savedHistory);

        // Act
        MedicalHistory result = medicalHistoryService.registerMedicalHistory(mockPatient, healthStatus);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        verify(medicalHistoryRepository).save(any(MedicalHistory.class));
    }

    @Test
    void registerMedicalHistory_WithNullHealthStatus_ShouldUseDefault() {
        // Arrange
        ArgumentCaptor<MedicalHistory> captor = ArgumentCaptor.forClass(MedicalHistory.class);
        when(medicalHistoryRepository.save(any(MedicalHistory.class))).thenAnswer(invocation -> {
            MedicalHistory mh = invocation.getArgument(0);
            mh.setId(5L);
            return mh;
        });

        // Act
        MedicalHistory result = medicalHistoryService.registerMedicalHistory(mockPatient, null);

        // Assert
        assertNotNull(result);
        assertEquals("Sin información", result.getCurrentHealthStatus());
        verify(medicalHistoryRepository).save(captor.capture());

        MedicalHistory captured = captor.getValue();
        assertEquals(mockPatient, captured.getPatient());
        assertEquals("Sin información", captured.getCurrentHealthStatus());
        assertNotNull(captured.getCreationDate());
        assertTrue(captured.getCreationDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
