package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.PrescribedMedicineRequestDTO;
import com.oncologic.clinic.dto.patient.response.PrescribedMedicineResponseDTO;
import com.oncologic.clinic.dto.patient.update.PrescribedMedicineUpdateDTO;
import com.oncologic.clinic.entity.patient.PrescribedMedicine;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.exception.runtime.patient.PrescribedMedicineNotFoundException;
import com.oncologic.clinic.mapper.patient.PrescribedMedicineMapper;
import com.oncologic.clinic.repository.patient.PrescribedMedicineRepository;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.service.patient.impl.PrescribedMedicineServiceImpl;
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
public class PrescribedMedicineServiceTest {

    @Mock
    private PrescribedMedicineRepository prescribedMedicineRepository;

    @Mock
    private TreatmentRepository treatmentRepository;

    @Mock
    private PrescribedMedicineMapper prescribedMedicineMapper;

    @InjectMocks
    private PrescribedMedicineServiceImpl prescribedMedicineService;

    private PrescribedMedicine createTestMedicine(Long id, String name, Treatment treatment) {
        PrescribedMedicine medicine = new PrescribedMedicine();
        medicine.setId(id);
        medicine.setMedicine(name);
        medicine.setTreatment(treatment);
        return medicine;
    }

    private PrescribedMedicineResponseDTO createTestMedicineDTO(Long id, String name) {
        return PrescribedMedicineResponseDTO.builder()
                .id(id)
                .medicine(name)
                .build();
    }

    private Treatment createTestTreatment(Long id) {
        Treatment treatment = new Treatment();
        treatment.setId(id);
        return treatment;
    }

    @Test
    void getPrescribedMedicineById_WhenIdExists_ReturnsPrescribedMedicineDTO() {
        // Arrange
        Long id = 1L;
        Treatment treatment = createTestTreatment(1L);
        PrescribedMedicine medicine = createTestMedicine(id, "Ibuprofen", treatment);
        PrescribedMedicineResponseDTO expectedDTO = createTestMedicineDTO(id, "Ibuprofen");

        when(prescribedMedicineRepository.findById(id)).thenReturn(Optional.of(medicine));
        when(prescribedMedicineMapper.toDto(medicine)).thenReturn(expectedDTO);

        // Act
        PrescribedMedicineResponseDTO result = prescribedMedicineService.getPrescribedMedicineById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Ibuprofen", result.getMedicine());

        verify(prescribedMedicineRepository, times(1)).findById(id);
        verify(prescribedMedicineMapper, times(1)).toDto(medicine);
    }

    @Test
    void getPrescribedMedicineById_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(prescribedMedicineRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PrescribedMedicineNotFoundException.class,
                () -> prescribedMedicineService.getPrescribedMedicineById(id));

        verify(prescribedMedicineRepository, times(1)).findById(id);
        verify(prescribedMedicineMapper, never()).toDto(any());
    }

    @Test
    void getAllPrescribedMedicines_WhenCalled_ReturnsPrescribedMedicineDTOList() {
        // Arrange
        Treatment treatment = createTestTreatment(1L);
        PrescribedMedicine medicine1 = createTestMedicine(1L, "Medicine 1", treatment);
        PrescribedMedicine medicine2 = createTestMedicine(2L, "Medicine 2", treatment);

        PrescribedMedicineResponseDTO dto1 = createTestMedicineDTO(1L, "Medicine 1");
        PrescribedMedicineResponseDTO dto2 = createTestMedicineDTO(2L, "Medicine 2");

        when(prescribedMedicineRepository.findAll()).thenReturn(Arrays.asList(medicine1, medicine2));
        when(prescribedMedicineMapper.toDto(medicine1)).thenReturn(dto1);
        when(prescribedMedicineMapper.toDto(medicine2)).thenReturn(dto2);

        // Act
        List<PrescribedMedicineResponseDTO> result = prescribedMedicineService.getAllPrescribedMedicines();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Medicine 1", result.get(0).getMedicine());
        assertEquals("Medicine 2", result.get(1).getMedicine());

        verify(prescribedMedicineRepository, times(1)).findAll();
        verify(prescribedMedicineMapper, times(1)).toDto(medicine1);
        verify(prescribedMedicineMapper, times(1)).toDto(medicine2);
    }

    @Test
    void createPrescribedMedicine_WhenValidData_ReturnsPrescribedMedicineDTO() {
        // Arrange
        Long treatmentId = 1L;
        Treatment treatment = createTestTreatment(treatmentId);

        PrescribedMedicineRequestDTO requestDTO = PrescribedMedicineRequestDTO.builder()
                .medicine("Paracetamol")
                .treatmentId(treatmentId)
                .build();

        PrescribedMedicine medicineToSave = createTestMedicine(null, "Paracetamol", treatment);
        PrescribedMedicine savedMedicine = createTestMedicine(1L, "Paracetamol", treatment);
        PrescribedMedicineResponseDTO responseDTO = createTestMedicineDTO(1L, "Paracetamol");

        when(treatmentRepository.findById(treatmentId)).thenReturn(Optional.of(treatment));
        when(prescribedMedicineMapper.toEntity(requestDTO)).thenReturn(medicineToSave);
        when(prescribedMedicineRepository.save(medicineToSave)).thenReturn(savedMedicine);
        when(prescribedMedicineMapper.toDto(savedMedicine)).thenReturn(responseDTO);

        // Act
        PrescribedMedicineResponseDTO result = prescribedMedicineService.createPrescribedMedicine(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Paracetamol", result.getMedicine());

        verify(treatmentRepository, times(1)).findById(treatmentId);
        verify(prescribedMedicineMapper, times(1)).toEntity(requestDTO);
        verify(prescribedMedicineRepository, times(1)).save(medicineToSave);
        verify(prescribedMedicineMapper, times(1)).toDto(savedMedicine);
    }

    @Test
    void updatePrescribedMedicine_WhenMedicineExists_ReturnsUpdatedMedicineDTO() {
        // Arrange
        Long id = 1L;
        Treatment treatment = createTestTreatment(1L);

        PrescribedMedicine existingMedicine = createTestMedicine(id, "Old Medicine", treatment);
        PrescribedMedicineUpdateDTO updateDTO = PrescribedMedicineUpdateDTO.builder()
                .medicine("New Medicine")
                .build();

        PrescribedMedicine updatedMedicine = createTestMedicine(id, "New Medicine", treatment);
        PrescribedMedicineResponseDTO responseDTO = createTestMedicineDTO(id, "New Medicine");

        when(prescribedMedicineRepository.findById(id)).thenReturn(Optional.of(existingMedicine));
        when(prescribedMedicineRepository.save(existingMedicine)).thenReturn(updatedMedicine);
        when(prescribedMedicineMapper.toDto(updatedMedicine)).thenReturn(responseDTO);

        // Act
        PrescribedMedicineResponseDTO result = prescribedMedicineService.updatePrescribedMedicine(id, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("New Medicine", result.getMedicine());

        verify(prescribedMedicineRepository, times(1)).findById(id);
        verify(prescribedMedicineRepository, times(1)).save(existingMedicine);
        verify(prescribedMedicineMapper, times(1)).toDto(updatedMedicine);
    }

    @Test
    void deletePrescribedMedicine_WhenIdExists_DeletesMedicine() {
        // Arrange
        Long id = 1L;
        Treatment treatment = createTestTreatment(1L);
        PrescribedMedicine medicine = createTestMedicine(id, "Medicine", treatment);

        when(prescribedMedicineRepository.findById(id)).thenReturn(Optional.of(medicine));

        // Act
        prescribedMedicineService.deletePrescribedMedicine(id);

        // Assert
        verify(prescribedMedicineRepository, times(1)).findById(id);
        verify(prescribedMedicineRepository, times(1)).delete(medicine);
    }
}