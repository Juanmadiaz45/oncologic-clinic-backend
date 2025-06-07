package com.oncologic.clinic.service.appointment;

import com.oncologic.clinic.dto.appointment.MedicalAppointmentDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalAppointmentResponseDTO;
import com.oncologic.clinic.dto.appointment.response.MedicalTaskResponseDTO;
import com.oncologic.clinic.dto.patient.response.ObservationResponseDTO;
import com.oncologic.clinic.dto.patient.response.TreatmentResponseDTO;
import com.oncologic.clinic.entity.appointment.MedicalAppointment;
import com.oncologic.clinic.entity.appointment.MedicalTask;

import java.util.List;
import java.util.Map;

public interface MedicalAppointmentService {
    MedicalAppointmentResponseDTO getMedicalAppointmentById(Long id);
    List<MedicalAppointmentResponseDTO> getAllMedicalAppointments();
    MedicalAppointmentResponseDTO createMedicalAppointment(MedicalAppointmentDTO dto);
    MedicalAppointmentResponseDTO updateMedicalAppointment(Long id, MedicalAppointmentDTO dto);
    void deleteMedicalAppointment(Long id);

    List<MedicalTaskResponseDTO> getAppointmentTasks(Long appointmentId);
    List<ObservationResponseDTO> getAppointmentObservations(Long appointmentId);
    List<TreatmentResponseDTO> getAppointmentTreatments(Long appointmentId);
    Map<String, Object> getAppointmentDetails(Long appointmentId);
    void startAppointment(Long appointmentId);
    void completeAppointment(Long appointmentId);

    List<MedicalAppointmentResponseDTO> getBaseAppointments();

    MedicalAppointment getMedicalAppointmentEntityById(Long id);

    Map<String, Object> getOrCreateAppointmentResult(Long appointmentId);

    List<Map<String, Object>> getTreatmentMedicines(Long treatmentId);

    MedicalTask getMedicalTaskEntityById(Long taskId);

}
