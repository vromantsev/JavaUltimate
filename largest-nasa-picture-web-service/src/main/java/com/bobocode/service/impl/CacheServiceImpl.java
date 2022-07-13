package com.bobocode.service.impl;

import com.bobocode.service.CacheService;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class CacheServiceImpl implements CacheService {

    private static final String DAILY_CRON = "0 0 1 ? * *";

    @Scheduled(cron = DAILY_CRON)
    @CacheEvict(value = "picture", allEntries = true)
    @Override
    public void clearCache() {
        log.warn("Clearing NASA picture cache...");
    }
}
