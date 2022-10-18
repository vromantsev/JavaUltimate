package com.bobocode.service;

import com.bobocode.config.AppProperties;
import com.bobocode.dto.Photo;
import com.bobocode.dto.Photos;
import com.bobocode.dto.PicData;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MarsLargestPictureServiceImpl implements MarsLargestPictureService {

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    @Override
    public PicData findLargestMarsPicture(int sol, String camera) {
        var url = buildUrl(sol, camera);
        final Photos photos = this.restTemplate.getForObject(url, Photos.class);
        return Objects.requireNonNull(photos).photos().parallelStream()
                .map(getPicDataFunction())
                .max(Comparator.comparing(PicData::contentLength))
                .orElseThrow();
    }

    private Function<Photo, PicData> getPicDataFunction() {
        return photo -> {
            final HttpHeaders originalHeaders = this.restTemplate.headForHeaders(photo.imgSrc());
            final URI location = originalHeaders.getLocation();
            final HttpHeaders redirectHeaders = this.restTemplate.headForHeaders(Objects.requireNonNull(location));
            final long contentLength = redirectHeaders.getContentLength();
            return new PicData(location.toString(), contentLength);
        };
    }

    private String buildUrl(int sol, String camera) {
        return UriComponentsBuilder.fromUriString(this.appProperties.getBaseUrl())
                .queryParam(this.appProperties.getSolHeader(), sol)
                .queryParam(this.appProperties.getApiKeyHeader(), this.appProperties.getApiKey())
                .queryParamIfPresent(this.appProperties.getCameraHeader(), Optional.ofNullable(camera))
                .toUriString();
    }
}
