package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.request.LaboratoryRequestDTO;
import com.oncologic.clinic.dto.examination.response.LaboratoryResponseDTO;
import com.oncologic.clinic.dto.examination.update.LaboratoryUpdateDTO;
import com.oncologic.clinic.entity.examination.Laboratory;

import java.util.List;

public interface LaboratoryService {
    LaboratoryResponseDTO getLaboratoryById(Long id);
    List<LaboratoryResponseDTO> getAllLaboratories();
    LaboratoryResponseDTO createLaboratory(LaboratoryRequestDTO requestDTO);
    LaboratoryResponseDTO updateLaboratory(Long id, LaboratoryUpdateDTO updateDTO);
    void deleteLaboratory(Long id);
}
