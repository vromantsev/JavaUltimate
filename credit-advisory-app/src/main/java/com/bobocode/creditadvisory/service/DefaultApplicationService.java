package com.bobocode.creditadvisory.service;

import com.bobocode.creditadvisory.dto.AssignmentResult;
import com.bobocode.creditadvisory.entity.Advisor;
import com.bobocode.creditadvisory.entity.Application;
import com.bobocode.creditadvisory.entity.Credit;
import com.bobocode.creditadvisory.exception.AdvisorNotFoundException;
import com.bobocode.creditadvisory.exception.ApplicationNotFoundException;
import com.bobocode.creditadvisory.exception.ExceptionMessages;
import com.bobocode.creditadvisory.repository.AdvisorRepository;
import com.bobocode.creditadvisory.repository.ApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class DefaultApplicationService implements ApplicationService {

    private static final Map<Advisor.Role, Credit> ROLE_CREDIT_MAP = new HashMap<>();

    private final AdvisorRepository advisorRepository;
    private final ApplicationRepository applicationRepository;

    static {
        ROLE_CREDIT_MAP.put(Advisor.Role.ASSOCIATE, new Credit(new BigDecimal(1), new BigDecimal(10000)));
        ROLE_CREDIT_MAP.put(Advisor.Role.PARTNER, new Credit(new BigDecimal(10001), new BigDecimal(50000)));
        ROLE_CREDIT_MAP.put(Advisor.Role.SENIOR, new Credit(new BigDecimal(50001), new BigDecimal(100000)));
    }

    public DefaultApplicationService(final AdvisorRepository advisorRepository,
                                     final ApplicationRepository applicationRepository) {
        this.advisorRepository = advisorRepository;
        this.applicationRepository = applicationRepository;
    }

    @Transactional
    @Override
    public AssignmentResult assignApplication(final Long advisorId) {
        Objects.requireNonNull(advisorId, ExceptionMessages.PARAMETER_NULL_CHECK_MSG.formatted("advisorId"));
        var advisor = this.advisorRepository.findById(advisorId)
                .orElseThrow(() -> new AdvisorNotFoundException(ExceptionMessages.ADVISOT_IS_NOT_FOUND_MSG.formatted(advisorId)));
        final Advisor.Role role = advisor.getRole();
        final Credit credit = ROLE_CREDIT_MAP.get(role);
        final Application application = this.applicationRepository.findFirstByAmountOfMoneyUSDBetween(credit.min(), credit.max())
                .orElseThrow(() -> new ApplicationNotFoundException(ExceptionMessages.APPLICATION_IS_NOT_FOUND_MSG));
        advisor.assignApplication(application);
        return AssignmentResult.builder()
                .id(application.getId())
                .amountOfMoneyUSD(application.getAmountOfMoneyUSD())
                .status(application.getStatus())
                .createdAt(application.getCreatedAt())
                .assignedAt(application.getAssignedAt())
                .applicantId(application.getApplicant().getId())
                .advisorId(application.getAdvisor().getId())
                .build();
    }
}
