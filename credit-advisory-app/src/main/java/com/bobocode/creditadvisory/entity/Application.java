package com.bobocode.creditadvisory.entity;

import com.bobocode.creditadvisory.exception.ApplicationIsAlreadyAssignedException;
import com.bobocode.creditadvisory.exception.ExceptionMessages;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"advisor", "applicant"})
@Entity
@Table(name = "application")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "application_id_gen")
    @SequenceGenerator(name = "application_id_gen", sequenceName = "application_seq", allocationSize = 1)
    private Long id;

    @Column(name = "amount_of_money_usd", nullable = false)
    private BigDecimal amountOfMoneyUSD;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @JoinColumn(name = "applicant_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Applicant applicant;

    @JoinColumn(name = "advisor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Advisor advisor;

    public enum Status {
        NEW, ASSIGNED, ON_HOLD, APPROVED, CANCELLED, DECLINED;
    }

    public void assign(final Advisor advisor) {
        if (getStatus() == Status.ASSIGNED) {
            throw new ApplicationIsAlreadyAssignedException(
                    ExceptionMessages.APPLICATION_IS_ALREADY_ASSIGNED_MSG.formatted(this.getId(), advisor.getId())
            );
        }
        this.setAdvisor(advisor);
        this.setStatus(Status.ASSIGNED);
        this.setAssignedAt(LocalDateTime.now());
    }
}
