package com.bobocode.shortener.repository;

import com.bobocode.shortener.entity.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {

    Optional<ShortenedUrl> findByShortUrlId(final String shortUrlId);

}
