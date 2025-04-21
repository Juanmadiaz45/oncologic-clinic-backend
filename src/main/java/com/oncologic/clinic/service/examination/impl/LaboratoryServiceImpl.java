package com.oncologic.clinic.service.examination.impl;

import com.oncologic.clinic.entity.examination.Laboratory;
import com.oncologic.clinic.service.examination.LaboratoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LaboratoryServiceImpl implements LaboratoryService {

    @Override
    public Laboratory getLaboratoryById(Long id) {
        return null;
    }

    @Override
    public List<Laboratory> getAllLaboratories() {
        return List.of();
    }

    @Override
    public Laboratory createLaboratory(Laboratory lab) {
        return null;
    }

    @Override
    public Laboratory updateLaboratory(Laboratory lab) {
        return null;
    }

    @Override
    public void deleteLaboratory(Long id) {

    }
}