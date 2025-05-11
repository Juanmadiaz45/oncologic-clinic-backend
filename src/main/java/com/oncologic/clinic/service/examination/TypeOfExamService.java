package com.oncologic.clinic.service.examination;

import com.oncologic.clinic.dto.examination.TypeOfExamDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;

import java.util.List;

public interface TypeOfExamService {
    TypeOfExamResponseDTO getTypeOfExamById(Long id);
    List<TypeOfExamResponseDTO> getAllTypesOfExam();
    TypeOfExamResponseDTO createTypeOfExam(TypeOfExamDTO requestDTO);
    TypeOfExamResponseDTO updateTypeOfExam(Long id, TypeOfExamDTO updateDTO);
    void deleteTypeOfExam(Long id);
}
