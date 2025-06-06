package com.oncologic.clinic.service.dashboard.doctor_dashboard.impl;

import com.oncologic.clinic.dto.dashboard.doctor_dashboard.AppointmentSummaryDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.DoctorDashboardMetricsDTO;
import com.oncologic.clinic.dto.dashboard.doctor_dashboard.NextAppointmentDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.personal.Doctor;
import com.oncologic.clinic.entity.user.User;
import com.oncologic.clinic.exception.runtime.dashboard.DashboardAccessException;
import com.oncologic.clinic.exception.runtime.personal.DoctorNotFoundException;
import com.oncologic.clinic.exception.runtime.dashboard.UserNotDoctorException;
import com.oncologic.clinic.exception.runtime.user.UserNotFoundException;
import com.oncologic.clinic.repository.appointment.MedicalAppointmentRepository;
import com.oncologic.clinic.repository.user.UserRepository;
import com.oncologic.clinic.service.dashboard.doctor_dashboard.interfaces.DoctorDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
public class DoctorDashboardServiceImpl implements DoctorDashboardService {

    private final UserRepository userRepository;
    private final MedicalAppointmentRepository appointmentRepository;

    @Autowired
    public DoctorDashboardServiceImpl(UserRepository userRepository, MedicalAppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public DoctorDashboardMetricsDTO getDashboardMetrics(String username) {
        log.debug("Getting dashboard metrics for user: {}", username);

        try {
            Doctor doctor = findAndValidateDoctor(username);

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

            // Count pending observations (simplified for now)
            int pendingObservations = calculatePendingObservations(doctor);

            log.debug("Dashboard metrics calculated successfully for doctor: {}", doctor.getId());

            return DoctorDashboardMetricsDTO.builder()
                    .appointmentsToday(appointmentsToday)
                    .activePatients(activePatients)
                    .pendingObservations(pendingObservations)
                    .currentDate(String.valueOf(today.getDayOfMonth()))
                    .currentDay(today.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")))
                    .build();

        } catch (UserNotFoundException | UserNotDoctorException | DoctorNotFoundException e) {
            log.warn("Access denied for dashboard metrics - username: {}, reason: {}", username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error getting dashboard metrics for user: {}", username, e);
            throw new DashboardAccessException("Unable to retrieve dashboard metrics at this time", e);
        }
    }

    @Override
    public List<AppointmentSummaryDTO> getTodayAppointments(String username, LocalDate date) {
        log.debug("Getting today's appointments for user: {} on date: {}", username, date);

        try {
            Doctor doctor = findAndValidateDoctor(username);

            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(23, 59, 59);

            List<MedicalAppointment> appointments = appointmentRepository
                    .findByDoctorAndAppointmentDateBetweenOrderByAppointmentDate(
                            doctor, startOfDay, endOfDay);

            List<AppointmentSummaryDTO> result = appointments.stream()
                    .map(this::mapToAppointmentSummary)
                    .collect(Collectors.toList());

            log.debug("Found {} appointments for doctor {} on {}", result.size(), doctor.getId(), date);
            return result;

        } catch (UserNotFoundException | UserNotDoctorException | DoctorNotFoundException e) {
            log.warn("Access denied for appointments - username: {}, reason: {}", username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error getting appointments for user: {} on date: {}", username, date, e);
            throw new DashboardAccessException("Unable to retrieve appointments at this time", e);
        }
    }

    @Override
    public NextAppointmentDTO getNextAppointment(String username) {
        log.debug("Getting next appointment for user: {}", username);

        try {
            Doctor doctor = findAndValidateDoctor(username);

            LocalDateTime now = LocalDateTime.now();
            List<MedicalAppointment> upcomingAppointments = appointmentRepository
                    .findByDoctorAndAppointmentDateAfterOrderByAppointmentDate(doctor, now);

            if (upcomingAppointments.isEmpty()) {
                log.debug("No upcoming appointments found for doctor: {}", doctor.getId());
                return null;
            }

            MedicalAppointment nextAppointment = upcomingAppointments.get(0);
            NextAppointmentDTO result = mapToNextAppointment(nextAppointment);

            log.debug("Next appointment found for doctor {}: {}", doctor.getId(), nextAppointment.getId());
            return result;

        } catch (UserNotFoundException | UserNotDoctorException | DoctorNotFoundException e) {
            log.warn("Access denied for next appointment - username: {}, reason: {}", username, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error getting next appointment for user: {}", username, e);
            throw new DashboardAccessException("Unable to retrieve next appointment at this time", e);
        }
    }

    /**
     * Finds and validates that the user exists and is a doctor
     *
     * @param username the username to validate
     * @return the Doctor entity
     * @throws UserNotFoundException   if user doesn't exist
     * @throws UserNotDoctorException  if a user is not a doctor
     * @throws DoctorNotFoundException if doctor data is inconsistent
     */
    private Doctor findAndValidateDoctor(String username) {
        // Check if user exists
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));

        // Check if user has personal data
        if (user.getPersonal() == null) {
            throw new UserNotDoctorException(username);
        }

        // Check if personal data is actually a doctor
        if (!(user.getPersonal() instanceof Doctor doctor)) {
            throw new UserNotDoctorException(username);
        }

        // Additional validation - ensure doctor data is complete
        if (doctor.getId() == null) {
            throw new DoctorNotFoundException(username);
        }

        return doctor;
    }

    /**
     * Calculate pending observations for a doctor
     * This is a simplified implementation - can be enhanced based on business logic
     */
    private int calculatePendingObservations(Doctor doctor) {
        try {
            // For now, return a fixed value
            // This can be replaced with actual business logic
            // Example: count appointments without observations, pending lab results, etc.
            return 5;
        } catch (Exception e) {
            log.warn("Error calculating pending observations for doctor {}: {}", doctor.getId(), e.getMessage());
            return 0; // Return 0 if calculation fails
        }
    }

    private AppointmentSummaryDTO mapToAppointmentSummary(MedicalAppointment appointment) {
        try {

            // Safely get office name
            String officeName = appointment.getMedicalOffice() == null ?
                    "No asignado" : appointment.getMedicalOffice().getName();

            // Safely get patient name
            String patientName = "Unknown Patient";
            if (appointment.getMedicalHistory() != null &&
                    appointment.getMedicalHistory().getPatient() != null) {
                patientName = appointment.getMedicalHistory().getPatient().getName();
            }

            // Safely get an appointment type
            String appointmentType = "Unknown Type";
            if (appointment.getTypeOfMedicalAppointment() != null) {
                appointmentType = appointment.getTypeOfMedicalAppointment().getName();
            }

            return AppointmentSummaryDTO.builder()
                    .id(appointment.getId())
                    .time(appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .patientName(patientName)
                    .appointmentType(appointmentType)
                    .status(determineAppointmentStatus(appointment))
                    .office(officeName)
                    .build();

        } catch (Exception e) {
            log.warn("Error mapping appointment to summary for appointment {}: {}",
                    appointment.getId(), e.getMessage());

            // Return a safe fallback
            return AppointmentSummaryDTO.builder()
                    .id(appointment.getId())
                    .time("--:--")
                    .patientName("Error loading data")
                    .appointmentType("Unknown")
                    .status("UNKNOWN")
                    .office("No asignado")
                    .build();
        }
    }

    private NextAppointmentDTO mapToNextAppointment(MedicalAppointment appointment) {
        try {

            // Safely get office name
            String officeName = appointment.getMedicalOffice() == null ?
                    "No asignado" : appointment.getMedicalOffice().getName();


            // Safely get patient name
            String patientName = "Unknown Patient";
            if (appointment.getMedicalHistory() != null &&
                    appointment.getMedicalHistory().getPatient() != null) {
                patientName = appointment.getMedicalHistory().getPatient().getName();
            }

            // Safely get an appointment type
            String appointmentType = "Unknown Type";
            if (appointment.getTypeOfMedicalAppointment() != null) {
                appointmentType = appointment.getTypeOfMedicalAppointment().getName();
            }

            return NextAppointmentDTO.builder()
                    .time(appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("HH:mm")))
                    .patientName(patientName)
                    .office(officeName)
                    .appointmentType(appointmentType)
                    .build();

        } catch (Exception e) {
            log.warn("Error mapping next appointment for appointment {}: {}",
                    appointment.getId(), e.getMessage());

            // Return a safe fallback
            return NextAppointmentDTO.builder()
                    .time("--:--")
                    .patientName("Error loading data")
                    .office("No asignado")
                    .appointmentType("Unknown")
                    .build();
        }
    }

    private String determineAppointmentStatus(MedicalAppointment appointment) {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime appointmentTime = appointment.getAppointmentDate();

            if (appointmentTime.isAfter(now)) {
                return "SCHEDULED";
            } else if (appointmentTime.isBefore(now.minusHours(1))) {
                return "COMPLETED";
            } else {
                return "IN_PROGRESS";
            }
        } catch (Exception e) {
            log.warn("Error determining appointment status for appointment {}: {}",
                    appointment.getId(), e.getMessage());
            return "UNKNOWN";
        }
    }
}