package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.dto.patient.request.AppointmentResultRequestDTO;
import com.oncologic.clinic.dto.patient.response.AppointmentResultResponseDTO;
import com.oncologic.clinic.dto.patient.update.AppointmentResultUpdateDTO;

import java.util.List;

public interface AppointmentResultService {
    AppointmentResultResponseDTO getAppointmentResultById(Long id);
    List<AppointmentResultResponseDTO> getAllAppointmentResults();
    AppointmentResultResponseDTO createAppointmentResult(AppointmentResultRequestDTO appointmentResultDTO);
    AppointmentResultResponseDTO updateAppointmentResult(Long id, AppointmentResultUpdateDTO appointmentResultDTO);
    void deleteAppointmentResult(Long id);
}
