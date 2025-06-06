package com.oncologic.clinic.service.appointment.impl;

import com.oncologic.clinic.dto.appointment.MedicalOfficeDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalOfficeResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalOffice;
import com.oncologic.clinic.exception.runtime.appointment.MedicalOfficeNotFoundException;
import com.oncologic.clinic.mapper.appointment.MedicalOfficeMapper;
import com.oncologic.clinic.repository.appointment.MedicalOfficeRepository;
import com.oncologic.clinic.service.appointment.MedicalOfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalOfficeServiceImpl implements MedicalOfficeService {

    private final MedicalOfficeRepository medicalOfficeRepository;
    private final MedicalOfficeMapper medicalOfficeMapper;

    @Override
    public MedicalOfficeResponseDTO createMedicalOffice(MedicalOfficeDTO dto) {
        MedicalOffice office = medicalOfficeMapper.toEntity(dto);
        MedicalOffice savedOffice = medicalOfficeRepository.save(office);
        return medicalOfficeMapper.toDto(savedOffice);
    }

    @Override
    public MedicalOfficeResponseDTO updateMedicalOffice(Long id, MedicalOfficeDTO dto) {
        MedicalOffice existingOffice = getMedicalOfficeEntityById(id);
        medicalOfficeMapper.updateEntityFromDto(dto, existingOffice);
        MedicalOffice updatedOffice = medicalOfficeRepository.save(existingOffice);
        return medicalOfficeMapper.toDto(updatedOffice);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalOfficeResponseDTO getMedicalOfficeById(Long id) {
        MedicalOffice office = getMedicalOfficeEntityById(id);
        return medicalOfficeMapper.toDto(office);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalOfficeResponseDTO> getAllMedicalOffices() {
        return medicalOfficeRepository.findAll().stream()
                .map(medicalOfficeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public MedicalOffice getMedicalOfficeEntityById(Long id) {
        return medicalOfficeRepository.findById(id)
                .orElseThrow(() -> new MedicalOfficeNotFoundException(id));
    }

    @Override
    public void deleteMedicalOffice(Long id) {
        MedicalOffice office = getMedicalOfficeEntityById(id);

        // CAMBIO: Verificar si tiene citas asignadas
        if (!office.getMedicalAppointments().isEmpty()) {
            throw new IllegalStateException(
                    "No se puede eliminar el consultorio porque tiene citas médicas asignadas");
        }

        medicalOfficeRepository.delete(office);
    }
}
