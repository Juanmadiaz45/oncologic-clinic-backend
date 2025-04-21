package com.oncologic.clinic.service.availability.impl;

import com.oncologic.clinic.entity.availability.Status;
import com.oncologic.clinic.service.availability.StatusService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService {

    @Override
    public Status getStatusById(Long id) {
        return null;
    }

    @Override
    public List<Status> getAllStatuses() {
        return List.of();
    }

    @Override
    public Status createStatus(Status status) {
        return null;
    }

    @Override
    public Status updateStatus(Status status) {
        return null;
    }

    @Override
    public void deleteStatus(Long id) {

    }
}