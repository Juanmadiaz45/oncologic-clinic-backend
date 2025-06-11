package com.oncologic.clinic.entity.personal;

import com.oncologic.clinic.entity.availability.Availability;
import com.oncologic.clinic.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "PERSONAL")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_number", nullable = false, length = 200)
    private String idNumber;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "last_name", nullable = false, length = 200)
    private String lastName;

    @Column(name = "email", nullable = false, length = 200)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 200)
    private String phoneNumber;

    @Column(name = "date_of_hiring", nullable = false)
    private LocalDateTime dateOfHiring;

    @Column(name = "status", nullable = false, length = 1)
    private Character status;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "PERSONAL_AVAILABILITIES",
            joinColumns = @JoinColumn(name = "personal_id"),
            inverseJoinColumns = @JoinColumn(name = "availability_id"))
    private Set<Availability> availabilities = new HashSet<>();

    public void addAvailability(Availability availability) {
        this.availabilities.add(availability);
        availability.getPersonals().add(this);
    }

    public void removeAvailability(Availability availability) {
        this.availabilities.remove(availability);
        availability.getPersonals().remove(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personal that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

