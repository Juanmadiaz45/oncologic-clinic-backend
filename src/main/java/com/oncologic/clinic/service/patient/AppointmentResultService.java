package com.oncologic.clinic.service.patient;

import com.oncologic.clinic.entity.patient.AppointmentResult;

import java.util.List;

public interface AppointmentResultService {
    AppointmentResult getAppointmentResultById(Long id);
    List<AppointmentResult> getAllAppointmentResults();
    AppointmentResult createAppointmentResult(AppointmentResult result);
    AppointmentResult updateAppointmentResult(AppointmentResult result);
    void deleteAppointmentResult(Long id);
}
