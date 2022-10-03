package com.bobocode.creditadvisory.repository;

import com.bobocode.creditadvisory.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query(
            "SELECT a FROM Application a " +
                    "WHERE a.status = 'NEW' AND a.amountOfMoneyUSD BETWEEN ?1 AND ?2 " +
                    "ORDER BY a.createdAt"
    )
    Optional<Application> findFirstByAmountOfMoneyUSDBetween(final BigDecimal min, final BigDecimal max);

}
