package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.PrescribedMedicineRequestDTO;
import com.oncologic.clinic.dto.patient.response.PrescribedMedicineResponseDTO;
import com.oncologic.clinic.dto.patient.update.PrescribedMedicineUpdateDTO;
import com.oncologic.clinic.entity.patient.PrescribedMedicine;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.mapper.patient.PrescribedMedicineMapper;
import com.oncologic.clinic.repository.patient.PrescribedMedicineRepository;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.service.patient.PrescribedMedicineService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrescribedMedicineServiceImpl implements PrescribedMedicineService {
    private final PrescribedMedicineRepository prescribedMedicineRepository;
    private final TreatmentRepository treatmentRepository;
    private final PrescribedMedicineMapper prescribedMedicineMapper;

    public PrescribedMedicineServiceImpl(PrescribedMedicineRepository prescribedMedicineRepository, TreatmentRepository treatmentRepository, PrescribedMedicineMapper prescribedMedicineMapper) {
        this.prescribedMedicineRepository = prescribedMedicineRepository;
        this.treatmentRepository = treatmentRepository;
        this.prescribedMedicineMapper = prescribedMedicineMapper;
    }

    @Override
    public PrescribedMedicineResponseDTO getPrescribedMedicineById(Long id) {
        PrescribedMedicine medicine = prescribedMedicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medicina prescrita no encontrada con el ID: " + id));
        return prescribedMedicineMapper.toDto(medicine);
    }

    @Override
    public List<PrescribedMedicineResponseDTO> getAllPrescribedMedicines() {
        return prescribedMedicineRepository.findAll().stream()
                .map(prescribedMedicineMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PrescribedMedicineResponseDTO createPrescribedMedicine(PrescribedMedicineRequestDTO dto) {
        Treatment treatment = treatmentRepository.findById(dto.getTreatmentId())
                .orElseThrow(() -> new EntityNotFoundException("Tratamiento no encontrado con el ID: " + dto.getTreatmentId()));

        PrescribedMedicine medicine = prescribedMedicineMapper.toEntity(dto);
        medicine.setTreatment(treatment);

        PrescribedMedicine saved = prescribedMedicineRepository.save(medicine);
        return prescribedMedicineMapper.toDto(saved);
    }

    @Override
    @Transactional
    public PrescribedMedicineResponseDTO updatePrescribedMedicine(Long id, PrescribedMedicineUpdateDTO dto) {
        PrescribedMedicine medicine = prescribedMedicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medicina prescrita no encontrada con el ID: " + id));

        prescribedMedicineMapper.updateEntityFromDto(dto, medicine);
        PrescribedMedicine updated = prescribedMedicineRepository.save(medicine);
        return prescribedMedicineMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deletePrescribedMedicine(Long id) {
        PrescribedMedicine medicine = prescribedMedicineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medicina prescrita no encontrada con el ID: " + id));
        prescribedMedicineRepository.delete(medicine);
    }
}