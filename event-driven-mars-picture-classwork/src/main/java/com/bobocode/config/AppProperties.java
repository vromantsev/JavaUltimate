package com.bobocode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    @Value("${app.queue-name}")
    private String queueName;

    @Value("${app.fanout-binding}")
    private String fanoutBinding;

    @Value("${app.result-queue}")
    private String resultQueue;

    @Value("${app.nasa.api.external.url}")
    private String baseUrl;

    @Value("${app.nasa.api.external.api-key}")
    private String apiKey;

    @Value("${app.nasa.api.headers.sol}")
    private String solHeader;

    @Value("${app.nasa.api.headers.camera}")
    private String cameraHeader;

    @Value("${app.nasa.api.headers.api-key}")
    private String apiKeyHeader;
}
