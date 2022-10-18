package com.bobocode.service;

import com.bobocode.config.AppProperties;
import com.bobocode.entity.Picture;
import com.bobocode.exceptions.PictureNotFoundException;
import com.bobocode.repository.NasaPictureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NasaProxyServiceImpl implements NasaProxyService {

    private final RestTemplate restTemplate;
    private final AppProperties appProperties;
    private final NasaPictureRepository nasaPictureRepository;

    @Cacheable("nasa-largest-picture")
    @Override
    public Picture proxyCallToNasa(int sol, String camera) {
        var url = buildUrl(sol, camera);
        final byte[] picture = Optional.ofNullable(this.restTemplate.getForObject(url, byte[].class))
                .orElseThrow(() -> new PictureNotFoundException("No pictures found!"));
        var key = UUID.randomUUID().toString();
        log.info("Generated key='{}' for picture with params: sol={}, camera='{}'", key, sol, camera);
        final Picture pic = this.nasaPictureRepository.save(
                Picture.builder()
                        .id(key)
                        .content(picture)
                        .build()
        );
        return pic;
    }

    @Override
    public byte[] getPictureFromCacheById(String id) {
        Objects.requireNonNull(id, "Parameter [id] must not be null!");
        final Picture picture = this.nasaPictureRepository.findById(id)
                .orElseThrow(() -> new PictureNotFoundException("Picture not found by id=%s".formatted(id)));
        return picture.getContent();
    }

    private String buildUrl(final int sol, final String camera) {
        return UriComponentsBuilder.fromUriString(this.appProperties.getBaseUrl())
                .queryParam(this.appProperties.getSolHeader(), sol)
                .queryParamIfPresent(this.appProperties.getCameraHeader(), Optional.ofNullable(camera))
                .toUriString();
    }//15e01c28-0c69-4092-a681-5001cce28af6
}
