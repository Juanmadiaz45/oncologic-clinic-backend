package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.request.TypeOfExamRequestDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.dto.examination.update.TypeOfExamUpdateDTO;
import com.oncologic.clinic.entity.examination.TypeOfExam;

import java.util.List;

public interface TypeOfExamService {
    TypeOfExamResponseDTO getTypeOfExamById(Long id);
    List<TypeOfExamResponseDTO> getAllTypesOfExam();
    TypeOfExamResponseDTO createTypeOfExam(TypeOfExamRequestDTO requestDTO);
    TypeOfExamResponseDTO updateTypeOfExam(Long id, TypeOfExamUpdateDTO updateDTO);
    void deleteTypeOfExam(Long id);
}
