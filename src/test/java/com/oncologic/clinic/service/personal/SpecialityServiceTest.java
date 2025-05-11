/*package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.SpecialityDTO;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.repository.personal.SpecialityRepository;
import com.oncologic.clinic.service.personal.impl.SpecialityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SpecialityServiceTest {
    @Mock
    private SpecialityRepository specialityRepository;

    @InjectMocks
    private SpecialityServiceImpl specialityService;

    private Speciality getMockSpeciality() {
        Speciality s = new Speciality();
        s.setId(1L);
        s.setName("Oncología");
        s.setDescription("Tratamiento del cáncer");
        return s;
    }

    private Doctor getMockDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dra. Pérez");
        return doctor;
    }

    @Test
    void getAllSpecialities_WhenCalled_ShouldReturnList() {
        // Arrange
        List<Speciality> specialities = List.of(getMockSpeciality());
        when(specialityRepository.findAll()).thenReturn(specialities);

        // Act
        List<Speciality> result = specialityService.getAllSpecialities();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Oncología", result.get(0).getName());
        verify(specialityRepository).findAll();
    }

    @Test
    void registerSpeciality_WhenValidDTO_ShouldSaveAndReturnSpeciality() {
        // Arrange
        SpecialityDTO dto = new SpecialityDTO();
        dto.setName("Pediatría");
        dto.setDescription("Atención a niños");

        Doctor doctor = getMockDoctor();

        Speciality saved = new Speciality();
        saved.setName(dto.getName());
        saved.setDescription(dto.getDescription());
        saved.setDoctor(doctor);

        when(specialityRepository.save(any(Speciality.class))).thenReturn(saved);

        // Act
        Speciality result = specialityService.registerSpeciality(dto, doctor);

        // Assert
        assertNotNull(result);
        assertEquals("Pediatría", result.getName());
        assertEquals("Atención a niños", result.getDescription());
        assertEquals(doctor, result.getDoctor());
        verify(specialityRepository).save(any(Speciality.class));
    }

    @Test
    void getSpecialityById_WhenFound_ShouldReturnSpeciality() {
        // Arrange
        Speciality speciality = getMockSpeciality();
        when(specialityRepository.findById(1L)).thenReturn(Optional.of(speciality));

        // Act
        Optional<Speciality> result = specialityService.getSpecialityById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Oncología", result.get().getName());
        verify(specialityRepository).findById(1L);
    }

    @Test
    void getSpecialityById_WhenNotFound_ShouldReturnEmpty() {
        // Arrange
        when(specialityRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Speciality> result = specialityService.getSpecialityById(999L);

        // Assert
        assertTrue(result.isEmpty());
        verify(specialityRepository).findById(999L);
    }

}*/
