package com.oncologic.clinic.service.patient.impl;

import com.oncologic.clinic.entity.patient.AppointmentResult;
import com.oncologic.clinic.service.patient.AppointmentResultService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentResultServiceImpl implements AppointmentResultService {

    @Override
    public AppointmentResult getAppointmentResultById(Long id) {
        return null;
    }

    @Override
    public List<AppointmentResult> getAllAppointmentResults() {
        return List.of();
    }

    @Override
    public AppointmentResult createAppointmentResult(AppointmentResult result) {
        return null;
    }

    @Override
    public AppointmentResult updateAppointmentResult(AppointmentResult result) {
        return null;
    }

    @Override
    public void deleteAppointmentResult(Long id) {

    }
}