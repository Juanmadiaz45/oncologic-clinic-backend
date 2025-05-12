package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.TypeOfMedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.TypeOfMedicalAppointmentResponseDTO;
import com.oncologic.clinic.entity.appointment.TypeOfMedicalAppointment;
import com.oncologic.clinic.mapper.appointment.TypeOfMedicalAppointmentMapper;
import com.oncologic.clinic.repository.appointment.TypeOfMedicalAppointmentRepository;
import com.oncologic.clinic.service.appointment.TypeOfMedicalAppointmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeOfMedicalAppointmentServiceImpl implements TypeOfMedicalAppointmentService {

    private final TypeOfMedicalAppointmentRepository repository;
    private final TypeOfMedicalAppointmentMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public TypeOfMedicalAppointmentResponseDTO getTypeOfMedicalAppointmentById(Long id) {
        TypeOfMedicalAppointment type = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Type of medical appointment not found"));
        return mapper.toDto(type);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypeOfMedicalAppointmentResponseDTO> getAllTypesOfMedicalAppointment() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TypeOfMedicalAppointmentResponseDTO createTypeOfMedicalAppointment(TypeOfMedicalAppointmentDTO dto) {
        TypeOfMedicalAppointment type = mapper.toEntity(dto);
        return mapper.toDto(repository.save(type));
    }

    @Override
    @Transactional
    public TypeOfMedicalAppointmentResponseDTO updateTypeOfMedicalAppointment(Long id, TypeOfMedicalAppointmentDTO dto) {
        TypeOfMedicalAppointment existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Type of medical appointment not found"));

        mapper.updateEntityFromDto(dto, existing);
        return mapper.toDto(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteTypeOfMedicalAppointment(Long id) {
        if(!repository.existsById(id)) {
            throw new EntityNotFoundException("Type of medical appointment not found");
        }
        repository.deleteById(id);
    }
}