package com.bobocode.shortener.service;

import com.bobocode.shortener.dto.URLPayload;
import com.bobocode.shortener.entity.ShortenedUrl;
import com.bobocode.shortener.repository.ShortenedUrlRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional(readOnly = true)
    @Override
    public String findOriginalURLById(final String urlId) {
        Objects.requireNonNull(urlId);
        final ShortenedUrl shortenedUrl = this.shortenedUrlRepository.findByShortUrl(urlId)
                .orElseThrow(() -> new IllegalArgumentException("Entity with id='%s' not found!".formatted(urlId)));
        return shortenedUrl.getOriginalUrl();
    }
}
