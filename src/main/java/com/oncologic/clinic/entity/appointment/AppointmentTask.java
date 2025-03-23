package com.oncologic.clinic.entity.appointment;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Entity
@Table(name = "APPOINTMENT_TASKS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTask {

    @EmbeddedId
    private AppointmentTaskId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicalAppointmentId")
    @JoinColumn(name = "medical_appointment_id")
    private MedicalAppointment medicalAppointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicalTaskId")
    @JoinColumn(name = "medical_task_id")
    private MedicalTask medicalTask;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppointmentTaskId implements java.io.Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Column(name = "medical_appointment_id")
        private Long medicalAppointmentId;

        @Column(name = "medical_task_id")
        private Long medicalTaskId;
    }
}