package com.oncologic.clinic.service.availability;

import com.oncologic.clinic.dto.availability.StatusDTO;
import com.oncologic.clinic.dto.availability.response.StatusResponseDTO;

import java.util.List;

public interface StatusService {
    StatusResponseDTO getStatusById(Long id);
    List<StatusResponseDTO> getAllStatuses();
    StatusResponseDTO createStatus(StatusDTO requestDTO);
    StatusResponseDTO updateStatus(Long id, StatusDTO updateDTO) ;
    void deleteStatus(Long id);
}
