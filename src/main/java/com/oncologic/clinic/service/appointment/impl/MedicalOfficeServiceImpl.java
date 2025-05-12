package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.exception.runtime.appointment.MedicalAppointmentNotFoundException;
import com.oncologic.clinic.exception.runtime.appointment.MedicalOfficeNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalOfficeMapper;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.appointment.MedicalOfficeRepository;
import com.oncologic.clinic.service.appointment.MedicalOfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalOfficeServiceImpl implements MedicalOfficeService {

    private final MedicalOfficeRepository repository;
    private final MedicalAppointmentRepository appointmentRepository;
    private final MedicalOfficeMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public MedicalOfficeResponseDTO getMedicalOfficeById(Long id) {
        MedicalOffice office = repository.findById(id)
                .orElseThrow(() -> new MedicalOfficeNotFoundException(id));
        return mapper.toDto(office);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalOfficeResponseDTO> getAllMedicalOffices() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MedicalOfficeResponseDTO createMedicalOffice(MedicalOfficeDTO dto) {
        MedicalOffice office = mapper.toEntity(dto);

        if (dto.getMedicalAppointmentId() != null) {
            office.setMedicalAppointment(appointmentRepository.findById(dto.getMedicalAppointmentId())
                    .orElseThrow(() -> new MedicalAppointmentNotFoundException(dto.getMedicalAppointmentId())));
        }

        return mapper.toDto(repository.save(office));
    }

    @Override
    @Transactional
    public MedicalOfficeResponseDTO updateMedicalOffice(Long id, MedicalOfficeDTO dto) {
        MedicalOffice existing = repository.findById(id)
                .orElseThrow(() -> new MedicalOfficeNotFoundException(id));

        mapper.updateEntityFromDto(dto, existing);

        if (dto.getMedicalAppointmentId() != null) {
            existing.setMedicalAppointment(appointmentRepository.findById(dto.getMedicalAppointmentId())
                    .orElseThrow(() -> new MedicalAppointmentNotFoundException(dto.getMedicalAppointmentId())));
        }

        return mapper.toDto(repository.save(existing));
    }

    @Override
    @Transactional
    public void deleteMedicalOffice(Long id) {
        if (!repository.existsById(id)) {
            throw new MedicalOfficeNotFoundException(id);
        }
        repository.deleteById(id);
    }
}