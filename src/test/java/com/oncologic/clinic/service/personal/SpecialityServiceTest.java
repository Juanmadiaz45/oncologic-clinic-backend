/*package com.oncologic.clinic.service.personal;

import com.oncologic.clinic.dto.personal.request.SpecialityRequestDTO;
import com.oncologic.clinic.dto.personal.response.SpecialityResponseDTO;
import com.oncologic.clinic.entity.personal.Speciality;
import com.oncologic.clinic.exception.runtime.personal.SpecialityNotFoundException;
import com.oncologic.clinic.mapper.personal.SpecialityMapper;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialityServiceTest {
    @Mock
    private SpecialityRepository specialityRepository;

    @Mock
    private SpecialityMapper specialityMapper;

    @InjectMocks
    private SpecialityServiceImpl specialityService;

    private Speciality getMockEntity() {
        Speciality s = new Speciality();
        s.setId(1L);
        s.setName("Oncología");
        s.setDescription("Tratamiento del cáncer");
        return s;
    }

    private SpecialityRequestDTO getMockRequestDTO() {
        return SpecialityRequestDTO.builder()
                .name("Oncología")
                .description("Tratamiento del cáncer")
                .build();
    }

    private SpecialityResponseDTO getMockResponseDTO() {
        return SpecialityResponseDTO.builder()
                .id(1L)
                .name("Oncología")
                .description("Tratamiento del cáncer")
                .build();
    }

    @Test
    void getAllSpecialities_ShouldReturnListOfSpecialityResponseDTO() {
        // Arrange
        List<Speciality> specialities = List.of(getMockEntity());
        when(specialityRepository.findAll()).thenReturn(specialities);
        when(specialityMapper.toDto(any(Speciality.class))).thenReturn(getMockResponseDTO());

        // Act
        List<SpecialityResponseDTO> result = specialityService.getAllSpecialities();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Oncología", result.get(0).getName());
        verify(specialityRepository).findAll();
        verify(specialityMapper).toDto(any(Speciality.class));
    }

    @Test
    void getSpecialityById_WhenFound_ShouldReturnResponseDTO() {
        // Arrange
        Speciality speciality = getMockEntity();
        when(specialityRepository.findById(1L)).thenReturn(Optional.of(speciality));
        when(specialityMapper.toDto(speciality)).thenReturn(getMockResponseDTO());

        // Act
        SpecialityResponseDTO result = specialityService.getSpecialityById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Oncología", result.getName());
        verify(specialityRepository).findById(1L);
        verify(specialityMapper).toDto(speciality);
    }

    @Test
    void getSpecialityById_WhenNotFound_ShouldThrowException() {
        // Arrange
        when(specialityRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(SpecialityNotFoundException.class, () -> specialityService.getSpecialityById(999L));
        verify(specialityRepository).findById(999L);
    }

    @Test
    void createSpeciality_WhenValidRequest_ShouldReturnSavedResponseDTO() {
        // Arrange
        SpecialityRequestDTO requestDTO = getMockRequestDTO();
        Speciality speciality = getMockEntity();
        Speciality savedSpeciality = getMockEntity(); // Simula entidad con ID
        SpecialityResponseDTO responseDTO = getMockResponseDTO();

        when(specialityMapper.toEntity(requestDTO)).thenReturn(speciality);
        when(specialityRepository.save(speciality)).thenReturn(savedSpeciality);
        when(specialityMapper.toDto(savedSpeciality)).thenReturn(responseDTO);

        // Act
        SpecialityResponseDTO result = specialityService.createSpeciality(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Oncología", result.getName());
        verify(specialityMapper).toEntity(requestDTO);
        verify(specialityRepository).save(speciality);
        verify(specialityMapper).toDto(savedSpeciality);
    }

    @Test
    void deleteSpeciality_WhenExists_ShouldDelete() {
        // Arrange
        when(specialityRepository.existsById(1L)).thenReturn(true);

        // Act
        specialityService.deleteSpeciality(1L);

        // Assert
        verify(specialityRepository).existsById(1L);
        verify(specialityRepository).deleteById(1L);
    }

    @Test
    void deleteSpeciality_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(specialityRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(SpecialityNotFoundException.class, () -> specialityService.deleteSpeciality(99L));
        verify(specialityRepository).existsById(99L);
        verify(specialityRepository, never()).deleteById(any());
    }

}*/
