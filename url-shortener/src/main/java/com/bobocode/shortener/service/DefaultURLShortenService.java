package com.bobocode.shortener.service;

import com.bobocode.shortener.dto.URLPayload;
import com.bobocode.shortener.entity.ShortenedUrl;
import com.bobocode.shortener.repository.ShortenedUrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DefaultURLShortenService implements URLShortenService {

    private final ShortenedUrlRepository shortenedUrlRepository;

    @Transactional
    @Override
    public String shortenUrl(final URLPayload payload) {
        Objects.requireNonNull(payload);
        var url = RandomStringUtils.randomAlphanumeric(10);
        var shortenedUrl = ShortenedUrl.builder()
                .shortUrlId(url)
                .originalUrl(payload.getUrl())
                .title(payload.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
        this.shortenedUrlRepository.save(shortenedUrl);
        return url;
    }

    @Cacheable("short-url-id")
    @Transactional(readOnly = true)
    @Override
    public String findOriginalURLByShortUrl(final String shortUrlId) {
        Objects.requireNonNull(shortUrlId);
        final ShortenedUrl shortenedUrl = this.shortenedUrlRepository.findByShortUrlId(shortUrlId)
                .orElseThrow(() -> new IllegalArgumentException("Entity with id='%s' not found!".formatted(shortUrlId)));
        return shortenedUrl.getOriginalUrl();
    }
}
