package com.bobocode.shortener.repository;

import com.bobocode.shortener.entity.ShortenedUrl;
import org.springframework.data.repository.CrudRepository;

public interface ShortenedUrlRepository extends CrudRepository<ShortenedUrl, String> {
}
