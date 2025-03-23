package com.oncologic.clinic.entity.personal;

import com.oncologic.clinic.entity.availability.Availability;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

@Entity
@Table(name = "PERSONAL_AVAILABILITIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalAvailability {

    @EmbeddedId
    private PersonalAvailabilityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("personalId")
    @JoinColumn(name = "personal_id")
    private Personal personal;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("availabilityId")
    @JoinColumn(name = "availability_id")
    private Availability availability;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonalAvailabilityId implements java.io.Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        @Column(name = "personal_id")
        private Long personalId;

        @Column(name = "availability_id")
        private Long availabilityId;
    }
}
