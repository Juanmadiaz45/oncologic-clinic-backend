package com.oncologic.clinic.service.availability;

import com.oncologic.clinic.entity.availability.Status;

import java.util.List;

public interface StatusService {
    Status getStatusById(Long id);
    List<Status> getAllStatuses();
    Status createStatus(Status status);
    Status updateStatus(Status status);
    void deleteStatus(Long id);
}
