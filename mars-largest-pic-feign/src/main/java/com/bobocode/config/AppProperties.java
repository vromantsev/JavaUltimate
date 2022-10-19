package com.bobocode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Getter
    @Value("${app.api.external.api-key}")
    private String apiKey;
}
