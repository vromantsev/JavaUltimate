package com.bobocode.mars.service;

import com.bobocode.mars.config.AppProperties;
import com.bobocode.mars.dto.Photo;
import com.bobocode.mars.dto.Photos;
import com.bobocode.mars.dto.PicData;
import com.bobocode.mars.exception.PictureNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Service
public class MarsPictureServiceImpl implements MarsPictureService {

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    public MarsPictureServiceImpl(RestTemplate restTemplate, AppProperties appProperties) {
        this.restTemplate = restTemplate;
        this.appProperties = appProperties;
    }

    @Cacheable("mars-largest_pic")
    @Override
    public PicData getLargestMarsPicture(int sol, String camera) {
        final String completeUrl = buildUrl(sol, camera);
        final Photos photos = this.restTemplate.getForObject(completeUrl, Photos.class);
        return Objects.requireNonNull(photos).photos().parallelStream()
                .map(createPicDataFunction())
                .max(Comparator.comparing(PicData::contentLength))
                .orElseThrow(() -> new PictureNotFoundException("No pictures found!"));
    }

    private Function<Photo, PicData> createPicDataFunction() {
        return photo -> {
            final String originalUrl = photo.imgSrc();
            final HttpHeaders headers = this.restTemplate.headForHeaders(originalUrl);
            final URI location = Objects.requireNonNull(headers.getLocation());
            final HttpHeaders picHeaders = this.restTemplate.headForHeaders(location);
            final long contentLength = picHeaders.getContentLength();
            return new PicData(originalUrl, contentLength, Objects.requireNonNull(this.restTemplate.getForObject(location, byte[].class)));
        };
    }

    private String buildUrl(final int sol, final String camera) {
        return UriComponentsBuilder.fromUriString(this.appProperties.getApiUrl())
                .queryParam(this.appProperties.getSolHeader(), sol)
                .queryParam(this.appProperties.getApiKeyHeader(), this.appProperties.getApiKey())
                .queryParamIfPresent(this.appProperties.getCameraHeader(), Optional.ofNullable(camera))
                .build()
                .toUriString();
    }
}
