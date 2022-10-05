package com.bobocode.mars.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Getter
    @Value("${app.api.external.url}")
    private String apiUrl;

    @Getter
    @Value("${app.api.external.api-key}")
    private String apiKey;

    @Getter
    @Value("${app.api.headers.sol}")
    private String solHeader;

    @Getter
    @Value("${app.api.headers.camera}")
    private String cameraHeader;

    @Getter
    @Value("${app.api.headers.api-key}")
    private String apiKeyHeader;
}
