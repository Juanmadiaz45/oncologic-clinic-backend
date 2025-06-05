package com.oncologic.clinic.service.dashboard.doctor_dashboard.impl;

import com.oncologic.clinic.dto.dashboard.doctor_dashboard.AppointmentSummaryDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.DoctorDashboardMetricsDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.NextAppointmentDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.patient.PatientRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.service.dashboard.doctor_dashboard.interfaces.DoctorDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class DoctorDashboardServiceImpl implements DoctorDashboardService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MedicalAppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;


    @Override
    public DoctorDashboardMetricsDTO getDashboardMetrics(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = user.getPersonal() instanceof Doctor ? (Doctor) user.getPersonal() : null;
        if (doctor == null) {
            throw new RuntimeException("User is not a doctor");
        }

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // Count today's appointments
        int appointmentsToday = appointmentRepository.countByDoctorAndAppointmentDateBetween(
                doctor, startOfDay, endOfDay);

        // Count active patients (patients with appointments in the last 30 days)
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        int activePatients = appointmentRepository.countDistinctPatientsByDoctorAndAppointmentDateAfter(
                doctor, thirtyDaysAgo);

        // Count pending observations (simplificado por ahora)
        int pendingObservations = 5; // Valor fijo por ahora

        return DoctorDashboardMetricsDTO.builder()
                .appointmentsToday(appointmentsToday)
                .activePatients(activePatients)
                .pendingObservations(pendingObservations)
                .currentDate(String.valueOf(today.getDayOfMonth()))
                .currentDay(today.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")))
                .build();
    }

    @Override
    public List<AppointmentSummaryDTO> getTodayAppointments(String username, LocalDate date) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = user.getPersonal() instanceof Doctor ? (Doctor) user.getPersonal() : null;
        if (doctor == null) {
            throw new RuntimeException("User is not a doctor");
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<MedicalAppointment> appointments = appointmentRepository
                .findByDoctorAndAppointmentDateBetweenOrderByAppointmentDate(
                        doctor, startOfDay, endOfDay);

        return appointments.stream()
                .map(this::mapToAppointmentSummary)
                .collect(Collectors.toList());
    }

    @Override
    public NextAppointmentDTO getNextAppointment(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Doctor doctor = user.getPersonal() instanceof Doctor ? (Doctor) user.getPersonal() : null;
        if (doctor == null) {
            throw new RuntimeException("User is not a doctor");
        }

        LocalDateTime now = LocalDateTime.now();
        List<MedicalAppointment> upcomingAppointments = appointmentRepository
                .findByDoctorAndAppointmentDateAfterOrderByAppointmentDate(doctor, now);

        if (upcomingAppointments.isEmpty()) {
            return null;
        }

        MedicalAppointment nextAppointment = upcomingAppointments.get(0);
        return mapToNextAppointment(nextAppointment);
    }

    private AppointmentSummaryDTO mapToAppointmentSummary(MedicalAppointment appointment) {
        String officeName = appointment.getMedicalOffices().isEmpty() ?
                "No asignado" : appointment.getMedicalOffices().get(0).getName();

        return AppointmentSummaryDTO.builder()
                .id(appointment.getId())
                .time(appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("HH:mm")))
                .patientName(appointment.getMedicalHistory().getPatient().getName())
                .appointmentType(appointment.getTypeOfMedicalAppointment().getName())
                .status(determineAppointmentStatus(appointment))
                .office(officeName)
                .build();
    }

    private NextAppointmentDTO mapToNextAppointment(MedicalAppointment appointment) {
        String officeName = appointment.getMedicalOffices().isEmpty() ?
                "No asignado" : appointment.getMedicalOffices().get(0).getName();

        return NextAppointmentDTO.builder()
                .time(appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("HH:mm")))
                .patientName(appointment.getMedicalHistory().getPatient().getName())
                .office(officeName)
                .appointmentType(appointment.getTypeOfMedicalAppointment().getName())
                .build();
    }

    private String determineAppointmentStatus(MedicalAppointment appointment) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentTime = appointment.getAppointmentDate();

        if (appointmentTime.isAfter(now)) {
            return "SCHEDULED";
        } else if (appointmentTime.isBefore(now.minusHours(1))) {
            return "COMPLETED";
        } else {
            return "IN_PROGRESS";
        }
    }
}
