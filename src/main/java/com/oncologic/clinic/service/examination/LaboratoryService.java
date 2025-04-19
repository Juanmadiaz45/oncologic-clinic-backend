package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.entity.examination.Laboratory;

import java.util.List;

public interface LaboratoryService {
    Laboratory getLaboratoryById(Long id);
    List<Laboratory> getAllLaboratories();
    Laboratory createLaboratory(Laboratory lab);
    Laboratory updateLaboratory(Laboratory lab);
    void deleteLaboratory(Long id);
}
