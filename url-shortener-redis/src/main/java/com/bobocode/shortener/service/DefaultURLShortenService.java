package com.bobocode.shortener.service;

import com.bobocode.shortener.dto.URLPayload;
import com.bobocode.shortener.entity.ShortenedUrl;
import com.bobocode.shortener.repository.ShortenedUrlRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class DefaultURLShortenService implements URLShortenService {

    private static final String URL_TEMPLATE = "https://boboshort.com/";

    private final ShortenedUrlRepository shortenedUrlRepository;

    public DefaultURLShortenService(final ShortenedUrlRepository shortenedUrlRepository) {
        this.shortenedUrlRepository = shortenedUrlRepository;
    }

    @Transactional
    @Override
    public String shortenUrl(final URLPayload payload) {
        Objects.requireNonNull(payload);
        var url = URL_TEMPLATE + RandomStringUtils.randomAlphanumeric(10);
        var shortenedUrl = ShortenedUrl.builder()
                .id(url)
                .originalUrl(payload.getUrl())
                .title(payload.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
        this.shortenedUrlRepository.save(shortenedUrl);
        return url;
    }

    @Transactional(readOnly = true)
    @Override
    public String findOriginalURLByShortUrl(final String shortUrlId) {
        Objects.requireNonNull(shortUrlId);
        final ShortenedUrl shortenedUrl = this.shortenedUrlRepository.findById(shortUrlId)
                .orElseThrow(() -> new IllegalArgumentException("Entity with id='%s' not found!".formatted(shortUrlId)));
        return shortenedUrl.getOriginalUrl();
    }
}
