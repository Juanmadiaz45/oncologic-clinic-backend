package com.oncologic.clinic.entity.personal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ADMINISTRATIVE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Administrative extends Personal {

    @Column(name = "position", nullable = false, length = 200)
    private String position;

    @Column(name = "department", nullable = false, length = 200)
    private String department;
}

