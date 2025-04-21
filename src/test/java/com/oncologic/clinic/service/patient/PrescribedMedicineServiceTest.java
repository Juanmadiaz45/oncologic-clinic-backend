package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.PrescribedMedicine;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.repository.patient.PrescribedMedicineRepository;
import com.oncologic.clinic.service.patient.impl.PrescribedMedicineServiceImpl;
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
public class PrescribedMedicineServiceTest {

    @Mock
    private PrescribedMedicineRepository prescribedMedicineRepository;

    @InjectMocks
    private PrescribedMedicineServiceImpl prescribedMedicineService;

    @Test
    void getPrescribedMedicineById_WhenIdExists_ReturnsPrescribedMedicine() {
        // Arrange
        Long id = 1L;
        PrescribedMedicine expectedMedicine = new PrescribedMedicine();
        expectedMedicine.setId(id);
        expectedMedicine.setMedicine("Ibuprofen");

        when(prescribedMedicineRepository.findById(id)).thenReturn(Optional.of(expectedMedicine));

        // Act
        PrescribedMedicine result = prescribedMedicineService.getPrescribedMedicineById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Ibuprofen", result.getMedicine());

        verify(prescribedMedicineRepository, times(1)).findById(id);
    }

    @Test
    void getPrescribedMedicineById_WhenIdDoesNotExist_ThrowsException() {
        // Arrange
        Long id = 99L;
        when(prescribedMedicineRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> prescribedMedicineService.getPrescribedMedicineById(id));

        verify(prescribedMedicineRepository, times(1)).findById(id);
    }

    @Test
    void getAllPrescribedMedicines_WhenCalled_ReturnsPrescribedMedicineList() {
        // Arrange
        PrescribedMedicine medicine1 = new PrescribedMedicine();
        medicine1.setId(1L);
        medicine1.setMedicine("Medicine 1");

        PrescribedMedicine medicine2 = new PrescribedMedicine();
        medicine2.setId(2L);
        medicine2.setMedicine("Medicine 2");

        List<PrescribedMedicine> expectedList = Arrays.asList(medicine1, medicine2);

        when(prescribedMedicineRepository.findAll()).thenReturn(expectedList);

        // Act
        List<PrescribedMedicine> result = prescribedMedicineService.getAllPrescribedMedicines();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Medicine 1", result.get(0).getMedicine());
        assertEquals("Medicine 2", result.get(1).getMedicine());

        verify(prescribedMedicineRepository, times(1)).findAll();
    }

    @Test
    void prescribeMedicine_WhenValidMedicine_ReturnsSavedMedicine() {
        // Arrange
        Treatment treatment = new Treatment();
        treatment.setId(1L);

        PrescribedMedicine medicineToSave = new PrescribedMedicine();
        medicineToSave.setMedicine("Paracetamol");
        medicineToSave.setTreatment(treatment);

        PrescribedMedicine savedMedicine = new PrescribedMedicine();
        savedMedicine.setId(1L);
        savedMedicine.setMedicine("Paracetamol");
        savedMedicine.setTreatment(treatment);

        when(prescribedMedicineRepository.save(medicineToSave)).thenReturn(savedMedicine);

        // Act
        PrescribedMedicine result = prescribedMedicineService.prescribeMedicine(medicineToSave);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Paracetamol", result.getMedicine());
        assertEquals(treatment, result.getTreatment());

        verify(prescribedMedicineRepository, times(1)).save(medicineToSave);
    }

    @Test
    void updatePrescribedMedicine_WhenMedicineExists_ReturnsUpdatedMedicine() {
        // Arrange
        Long id = 1L;
        Treatment treatment = new Treatment();
        treatment.setId(1L);

        PrescribedMedicine existingMedicine = new PrescribedMedicine();
        existingMedicine.setId(id);
        existingMedicine.setMedicine("Old Medicine");
        existingMedicine.setTreatment(treatment);

        PrescribedMedicine updatedMedicine = new PrescribedMedicine();
        updatedMedicine.setId(id);
        updatedMedicine.setMedicine("New Medicine");
        updatedMedicine.setTreatment(treatment);

        when(prescribedMedicineRepository.findById(id)).thenReturn(Optional.of(existingMedicine));
        when(prescribedMedicineRepository.save(existingMedicine)).thenReturn(updatedMedicine);

        // Act
        PrescribedMedicine result = prescribedMedicineService.updatePrescribedMedicine(updatedMedicine);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("New Medicine", result.getMedicine());

        verify(prescribedMedicineRepository, times(1)).findById(id);
        verify(prescribedMedicineRepository, times(1)).save(existingMedicine);
    }

    @Test
    void deletePrescribedMedicine_WhenIdExists_DeletesMedicine() {
        // Arrange
        Long id = 1L;
        PrescribedMedicine medicine = new PrescribedMedicine();
        medicine.setId(id);

        when(prescribedMedicineRepository.findById(id)).thenReturn(Optional.of(medicine));

        // Act
        prescribedMedicineService.deletePrescribedMedicine(id);

        // Assert
        verify(prescribedMedicineRepository, times(1)).findById(id);
        verify(prescribedMedicineRepository, times(1)).delete(medicine);
    }
}