package com.bobocode.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AppProperties {

    @Value("${app.nasa.api.url}")
    private String baseUrl;

    @Value("${app.nasa.api.headers.sol}")
    private String solHeader;

    @Value("${app.nasa.api.headers.camera}")
    private String cameraHeader;
}
