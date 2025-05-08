package com.oncologic.clinic.controller.rest.examination;

import com.oncologic.clinic.dto.examination.request.TypeOfExamRequestDTO;
import com.oncologic.clinic.dto.examination.response.TypeOfExamResponseDTO;
import com.oncologic.clinic.dto.examination.update.TypeOfExamUpdateDTO;
import com.oncologic.clinic.service.examination.TypeOfExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/types-of-exam")
@RequiredArgsConstructor
public class TypeOfExamRestController {

    private final TypeOfExamService typeOfExamService;

    @GetMapping("/{id}")
    public ResponseEntity<TypeOfExamResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(typeOfExamService.getTypeOfExamById(id));
    }

    @GetMapping
    public ResponseEntity<List<TypeOfExamResponseDTO>> getAll() {
        return ResponseEntity.ok(typeOfExamService.getAllTypesOfExam());
    }

    @PostMapping
    public ResponseEntity<TypeOfExamResponseDTO> create(
            @Valid @RequestBody TypeOfExamRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(typeOfExamService.createTypeOfExam(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeOfExamResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody TypeOfExamUpdateDTO updateDTO) {
        return ResponseEntity.ok(typeOfExamService.updateTypeOfExam(id, updateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        typeOfExamService.deleteTypeOfExam(id);
        return ResponseEntity.noContent().build();
    }
}