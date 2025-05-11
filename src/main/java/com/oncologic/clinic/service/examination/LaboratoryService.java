package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.LaboratoryDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;

import java.util.List;

public interface LaboratoryService {
    LaboratoryResponseDTO getLaboratoryById(Long id);
    List<LaboratoryResponseDTO> getAllLaboratories();
    LaboratoryResponseDTO createLaboratory(LaboratoryDTO requestDTO);
    LaboratoryResponseDTO updateLaboratory(Long id, LaboratoryDTO updateDTO);
    void deleteLaboratory(Long id);
}
