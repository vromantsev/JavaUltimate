package com.bobocode.reactive.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Value("${nasa.api.url}")
    private String nasaApiUrl;

    @Value("${nasa.api.key}")
    private String apiKey;

    public String getNasaApiUrl() {
        return nasaApiUrl;
    }

    public void setNasaApiUrl(String nasaApiUrl) {
        this.nasaApiUrl = nasaApiUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
