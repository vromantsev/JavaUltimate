package com.bobocode.service;

import com.bobocode.config.AppProperties;
import com.bobocode.dto.*;
import com.bobocode.exceptions.PictureNotFoundException;
import com.bobocode.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class MarsPictureServiceImpl implements MarsPictureService {

    private final RestTemplate restTemplate;
    private final MessageService messageService;
    private final AppProperties appProperties;
    private final PictureRepository pictureRepository;

    @Override
    public String generateCommandId(final PictureRequest request) {
        Objects.requireNonNull(request, "Parameter [request] must not be null!");
        final String commandId = RandomStringUtils.randomAlphanumeric(10);
        log.info("Generated commandId='{}' for request='{}'", commandId, request);
        this.messageService.sendMessage(new PictureCommandDetails(commandId, request));
        return commandId;
    }

    @Cacheable("picture-by-command-id")
    @Override
    public URI getPictureUriByCommandId(final String commandId) {
        Objects.requireNonNull(commandId, "Parameter [commandId] must not be null!");
        return this.pictureRepository.getPicture(commandId);
    }

    @Cacheable("picture")
    @Override
    public byte[] downloadPicture(final URI url) {
        Objects.requireNonNull(url, "Parameter [url] must not be null!");
        return Optional.ofNullable(this.restTemplate.getForObject(url, byte[].class))
                .orElseThrow(() -> new PictureNotFoundException("No pictures found!"));
    }

    @Cacheable("mars-largest-picture")
    @Override
    public PicData getLargestMarsPicture(final PictureRequest request) {
        Objects.requireNonNull(request, "Parameter [request] must not be null!");
        var photosUrl = buildUrl(request);
        final Photos photos = this.restTemplate.getForObject(photosUrl, Photos.class);
        return Objects.requireNonNull(photos).photos().parallelStream()
                .map(getPicDataFunction())
                .max(Comparator.comparing(PicData::contentLength))
                .orElseThrow(() -> new PictureNotFoundException("No pictures found!"));
    }

    private Function<Photo, PicData> getPicDataFunction() {
        return photo -> {
            final HttpHeaders headers = this.restTemplate.headForHeaders(photo.imgSrc());
            final long contentLength = headers.getContentLength();
            return new PicData(photo.imgSrc(), contentLength);
        };
    }

    private String buildUrl(final PictureRequest request) {
        return UriComponentsBuilder.fromUriString(this.appProperties.getBaseUrl())
                .queryParam(this.appProperties.getSolHeader(), request.sol())
                .queryParam(this.appProperties.getApiKeyHeader(), this.appProperties.getApiKey())
                .queryParamIfPresent(this.appProperties.getCameraHeader(), Optional.ofNullable(request.camera()))
                .toUriString();
    }
}
