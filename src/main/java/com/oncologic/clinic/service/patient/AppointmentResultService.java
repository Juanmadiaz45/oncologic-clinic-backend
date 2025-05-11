package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.AppointmentResultRequestDTO;
import com.oncologic.clinic.dto.patient.response.AppointmentResultResponseDTO;

import java.util.List;

public interface AppointmentResultService {
    AppointmentResultResponseDTO getAppointmentResultById(Long id);
    List<AppointmentResultResponseDTO> getAllAppointmentResults();
    AppointmentResultResponseDTO createAppointmentResult(AppointmentResultRequestDTO appointmentResultDTO);
    AppointmentResultResponseDTO updateAppointmentResult(Long id, AppointmentResultRequestDTO appointmentResultDTO);
    void deleteAppointmentResult(Long id);
}
