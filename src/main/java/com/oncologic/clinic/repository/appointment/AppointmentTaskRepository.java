package com.oncologic.clinic.repository.appointment;

import com.oncologic.clinic.entity.appointment.AppointmentTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentTaskRepository extends JpaRepository<AppointmentTask, AppointmentTask.AppointmentTaskId> {
}
