package com.oncologic.clinic.dto.examination.response;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LaboratoryResponseDTO {
    private Long id;
    private String name;
    private String location;
    private String telephone;
}