package com.bobocode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    @Value("${app.nasa.api.url}")
    private String baseUrl;

    @Value("${app.nasa.api.api-key}")
    private String apiKey;

    @Value("${app.nasa.headers.sol}")
    private String solHeader;

    @Value("${app.nasa.headers.api-key}")
    private String apiKeyHeader;

    @Value("${app.nasa.headers.camera}")
    private String cameraHeader;
}
