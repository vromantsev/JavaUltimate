package com.bobocode.creditadvisory.entity;

import com.bobocode.creditadvisory.exception.AdvisorHasAssignedApplicationException;
import com.bobocode.creditadvisory.exception.ExceptionMessages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "advisor")
public class Advisor extends SystemUser {

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "advisor")
    private Set<Application> applications = new HashSet<>();

    private void checkIfApplicationIsAssigned() {
        var hasApplicationAssigned = this.applications.stream()
                .anyMatch(application -> application.getStatus() == Application.Status.ASSIGNED);
        if (hasApplicationAssigned) {
            throw new AdvisorHasAssignedApplicationException(
                    ExceptionMessages.ADVISOR_HAS_APPLICATION_ASSIGNED_MSG.formatted(this.getId())
            );
        }
    }

    public void assignApplication(final Application application) {
        checkIfApplicationIsAssigned();
        application.assign(this);
        this.applications.add(application);
    }

    public enum Role {
        ASSOCIATE, PARTNER, SENIOR;
    }
}
