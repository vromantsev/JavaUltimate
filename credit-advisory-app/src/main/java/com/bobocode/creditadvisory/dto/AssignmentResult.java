package com.bobocode.creditadvisory.dto;

import com.bobocode.creditadvisory.entity.Application;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssignmentResult {
    private Long id;
    private BigDecimal amountOfMoneyUSD;
    private Application.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;
    private Long applicantId;
    private Long advisorId;
}
