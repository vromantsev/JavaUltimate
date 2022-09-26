package com.bobocode.shortener.service;

import com.bobocode.shortener.dto.URLPayload;
import com.bobocode.shortener.entity.ShortenedUrl;
import com.bobocode.shortener.repository.ShortenedUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class DefaultURLShortenService implements URLShortenService {

    private static final String URL_TEMPLATE = "boboshort.";
    private final ShortenedUrlRepository shortenedUrlRepository;

    @Transactional
    @Override
    public String shortenUrl(final URLPayload payload) {
        Objects.requireNonNull(payload);
        final int randomValue = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        var url = URL_TEMPLATE + randomValue;
        var shortenedUrl = ShortenedUrl.builder()
                .shortUrl(url)
                .originalUrl(payload.getUrl())
                .title(payload.getTitle())
                .createdAt(LocalDateTime.now())
                .build();
        this.shortenedUrlRepository.save(shortenedUrl);
        return url;
    }

    @Cacheable("short-url")
    @Transactional(readOnly = true)
    @Override
    public String findOriginalURLByShortUrl(final String shortUrl) {
        Objects.requireNonNull(shortUrl);
        final ShortenedUrl shortenedUrl = this.shortenedUrlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new IllegalArgumentException("Entity with id='%s' not found!".formatted(shortUrl)));
        return shortenedUrl.getOriginalUrl();
    }
}
