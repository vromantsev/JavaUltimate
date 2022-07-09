package com.bobocode.service;

import com.bobocode.configuration.AppProperties;
import com.bobocode.domain.PicData;
import com.bobocode.domain.Picture;
import com.bobocode.domain.Pictures;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String BASE_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";
    private static final String SOL = "sol";
    private static final String API_KEY = "api_key";

    private final AppProperties appProperties;
    private final RestTemplate httpClient;

    @Autowired
    public PictureServiceImpl(final AppProperties appProperties,
                              final RestTemplate httpClient) {
        this.appProperties = appProperties;
        this.httpClient = httpClient;
    }

    @Override
    public PicData getLargestPicture(final String sol) {
        Objects.requireNonNull(sol, "Sol parameter is supposed to be provided!");
        final URI uri = buildUri(sol);
        final Pictures pictures = this.httpClient.getForObject(uri, Pictures.class);
        return Objects.requireNonNull(pictures).photos()
                .parallelStream()
                .map(Picture::imgSrc)
                .map(toPicDataMappingFunction())
                .max(Comparator.comparing(PicData::originalUrl))
                .orElseThrow();
    }

    private Function<String, PicData> toPicDataMappingFunction() {
        return originalUrl -> {
            final HttpHeaders originalHeaders = this.httpClient.headForHeaders(originalUrl);
            final URI location = Objects.requireNonNull(originalHeaders.getLocation());
            final HttpHeaders headers = this.httpClient.headForHeaders(location);
            final long contentLength = headers.getContentLength();
            return new PicData(originalUrl, contentLength);
        };
    }

    private URI buildUri(final String sol) {
        return UriComponentsBuilder.fromUriString(BASE_URL)
                .queryParam(SOL, sol)
                .queryParam(API_KEY, this.appProperties.getApiKey())
                .build()
                .toUri();
    }
}
