package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.dto.patient.request.PrescribedMedicineRequestDTO;
import com.oncologic.clinic.dto.patient.response.PrescribedMedicineResponseDTO;
import com.oncologic.clinic.dto.patient.update.PrescribedMedicineUpdateDTO;
import com.oncologic.clinic.entity.patient.PrescribedMedicine;
import com.oncologic.clinic.entity.patient.Treatment;
import com.oncologic.clinic.exception.runtime.patient.PrescribedMedicineCreationException;
import com.oncologic.clinic.exception.runtime.patient.PrescribedMedicineNotFoundException;
import com.oncologic.clinic.exception.runtime.patient.TreatmentNotFoundException;
import com.oncologic.clinic.mapper.patient.PrescribedMedicineMapper;
import com.oncologic.clinic.repository.patient.PrescribedMedicineRepository;
import com.oncologic.clinic.repository.patient.TreatmentRepository;
import com.oncologic.clinic.service.patient.PrescribedMedicineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PrescribedMedicineServiceImpl implements PrescribedMedicineService {
    private static final Logger logger = LoggerFactory.getLogger(PrescribedMedicineServiceImpl.class);

    private final PrescribedMedicineRepository prescribedMedicineRepository;
    private final TreatmentRepository treatmentRepository;
    private final PrescribedMedicineMapper prescribedMedicineMapper;

    public PrescribedMedicineServiceImpl(PrescribedMedicineRepository prescribedMedicineRepository,
                                         TreatmentRepository treatmentRepository,
                                         PrescribedMedicineMapper prescribedMedicineMapper) {
        this.prescribedMedicineRepository = prescribedMedicineRepository;
        this.treatmentRepository = treatmentRepository;
        this.prescribedMedicineMapper = prescribedMedicineMapper;
    }

    @Override
    public PrescribedMedicineResponseDTO getPrescribedMedicineById(Long id) {
        logger.info("Fetching prescribed medicine with ID: {}", id);
        try {
            PrescribedMedicine medicine = prescribedMedicineRepository.findById(id)
                    .orElseThrow(() -> new PrescribedMedicineNotFoundException(id));
            return prescribedMedicineMapper.toDto(medicine);
        } catch (PrescribedMedicineNotFoundException e) {
            logger.error("Error fetching prescribed medicine: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<PrescribedMedicineResponseDTO> getAllPrescribedMedicines() {
        logger.info("Retrieving all prescribed medicines");
        try {
            List<PrescribedMedicineResponseDTO> medicines = prescribedMedicineRepository.findAll().stream()
                    .map(prescribedMedicineMapper::toDto)
                    .toList();

            if (medicines.isEmpty()) {
                logger.info("No prescribed medicines found in the database");
            }
            return medicines;
        } catch (Exception e) {
            logger.error("Error retrieving all prescribed medicines: {}", e.getMessage());
            throw new RuntimeException("Error retrieving prescribed medicines", e);
        }
    }

    @Override
    @Transactional
    public PrescribedMedicineResponseDTO createPrescribedMedicine(PrescribedMedicineRequestDTO dto) {
        logger.info("Creating new prescribed medicine for treatment ID: {}", dto.getTreatmentId());
        try {
            Treatment treatment = treatmentRepository.findById(dto.getTreatmentId())
                    .orElseThrow(() -> new TreatmentNotFoundException(dto.getTreatmentId()));

            PrescribedMedicine medicine = prescribedMedicineMapper.toEntity(dto);
            medicine.setTreatment(treatment);

            PrescribedMedicine saved = prescribedMedicineRepository.save(medicine);
            logger.info("Prescribed medicine created with ID: {}", saved.getId());
            return prescribedMedicineMapper.toDto(saved);

        } catch (TreatmentNotFoundException e) {
            logger.error("Treatment not found: {}", e.getMessage());
            throw e;
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation when creating prescribed medicine: {}", e.getMessage());
            throw new PrescribedMedicineCreationException("Data integrity error when creating prescribed medicine");
        } catch (Exception e) {
            logger.error("Unexpected error when creating prescribed medicine: {}", e.getMessage());
            throw new PrescribedMedicineCreationException("Error creating prescribed medicine: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public PrescribedMedicineResponseDTO updatePrescribedMedicine(Long id, PrescribedMedicineUpdateDTO dto) {
        logger.info("Updating prescribed medicine with ID: {}", id);
        try {
            PrescribedMedicine medicine = prescribedMedicineRepository.findById(id)
                    .orElseThrow(() -> new PrescribedMedicineNotFoundException(id));

            prescribedMedicineMapper.updateEntityFromDto(dto, medicine);
            PrescribedMedicine updated = prescribedMedicineRepository.save(medicine);
            logger.info("Prescribed medicine updated with ID: {}", id);
            return prescribedMedicineMapper.toDto(updated);

        } catch (PrescribedMedicineNotFoundException e) {
            logger.error("Prescribed medicine not found: {}", e.getMessage());
            throw e;
        } catch (DataIntegrityViolationException e) {
            logger.error("Data integrity violation when updating prescribed medicine: {}", e.getMessage());
            throw new RuntimeException("Data integrity error when updating prescribed medicine", e);
        } catch (Exception e) {
            logger.error("Unexpected error when updating prescribed medicine: {}", e.getMessage());
            throw new RuntimeException("Error updating prescribed medicine", e);
        }
    }

    @Override
    @Transactional
    public void deletePrescribedMedicine(Long id) {
        logger.info("Deleting prescribed medicine with ID: {}", id);
        try {
            PrescribedMedicine medicine = prescribedMedicineRepository.findById(id)
                    .orElseThrow(() -> new PrescribedMedicineNotFoundException(id));

            prescribedMedicineRepository.delete(medicine);
            logger.info("Prescribed medicine deleted with ID: {}", id);

        } catch (PrescribedMedicineNotFoundException e) {
            logger.error("Prescribed medicine not found: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error when deleting prescribed medicine: {}", e.getMessage());
            throw new RuntimeException("Error deleting prescribed medicine", e);
        }
    }
}