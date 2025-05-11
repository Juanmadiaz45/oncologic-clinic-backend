/*package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import com.oncologic.clinic.repository.appointment.TypeOfMedicalAppointmentRepository;
import com.oncologic.clinic.service.appointment.impl.TypeOfMedicalAppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeOfMedicalAppointmentServiceTest {

    @Mock
    private TypeOfMedicalAppointmentRepository repository;

    @InjectMocks
    private TypeOfMedicalAppointmentServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTypeOfMedicalAppointmentById_WhenIdExists_ReturnsType() {
        // Arrange
        Long id = 1L;
        TypeOfMedicalAppointment type = new TypeOfMedicalAppointment(id, "Oncología", List.of());
        when(repository.findById(id)).thenReturn(Optional.of(type));

        // Act
        TypeOfMedicalAppointment result = service.getTypeOfMedicalAppointmentById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Oncología", result.getName());
    }

    @Test
    void getAllTypesOfMedicalAppointment_WhenCalled_ReturnsListOfTypes() {
        // Arrange
        List<TypeOfMedicalAppointment> types = Arrays.asList(
                new TypeOfMedicalAppointment(1L, "Oncología", List.of()),
                new TypeOfMedicalAppointment(2L, "Dermatología", List.of())
        );
        when(repository.findAll()).thenReturn(types);

        // Act
        List<TypeOfMedicalAppointment> result = service.getAllTypesOfMedicalAppointment();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Oncología", result.get(0).getName());
        assertEquals("Dermatología", result.get(1).getName());
    }

    @Test
    void createTypeOfMedicalAppointment_WhenValidInput_ReturnsCreatedType() {
        // Arrange
        TypeOfMedicalAppointment input = new TypeOfMedicalAppointment(null, "Neurología", List.of());
        TypeOfMedicalAppointment saved = new TypeOfMedicalAppointment(1L, "Neurología", List.of());
        when(repository.save(input)).thenReturn(saved);

        // Act
        TypeOfMedicalAppointment result = service.createTypeOfMedicalAppointment(input);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Neurología", result.getName());
    }

    @Test
    void updateTypeOfMedicalAppointment_WhenValidInput_ReturnsUpdatedType() {
        // Arrange
        TypeOfMedicalAppointment updated = new TypeOfMedicalAppointment(1L, "Ginecología", List.of());
        when(repository.save(updated)).thenReturn(updated);

        // Act
        TypeOfMedicalAppointment result = service.updateTypeOfMedicalAppointment(updated);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Ginecología", result.getName());
    }

    @Test
    void deleteTypeOfMedicalAppointment_WhenIdExists_DeletesSuccessfully() {
        // Arrange
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        // Act
        service.deleteTypeOfMedicalAppointment(id);

        // Assert
        verify(repository, times(1)).deleteById(id);
    }
}
 */
