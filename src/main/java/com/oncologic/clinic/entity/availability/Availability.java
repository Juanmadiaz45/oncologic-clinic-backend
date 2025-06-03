package com.oncologic.clinic.entity.availability;

import com.oncologic.clinic.entity.personal.Personal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AVAILABILITIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Availability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @ManyToMany(mappedBy = "availabilities")
    private Set<Personal> personals = new HashSet<>();

    @ManyToOne // Many availabilities can have the same status
    @JoinColumn(name = "status_id") // FK column in the AVAILABILITIES table
    private Status status;
}

