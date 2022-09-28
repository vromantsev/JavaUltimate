package com.bobocode.shortener.controller;

import com.bobocode.shortener.dto.URLPayload;
import com.bobocode.shortener.service.URLShortenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class URLShortenerController {

    private final URLShortenService urlShortenService;

    @PostMapping("/short/urls")
    public ResponseEntity<?> shortenUrl(@RequestBody final URLPayload payload) {
        final String shortenUrl = this.urlShortenService.shortenUrl(payload);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, shortenUrl)
                .build();
    }

    @GetMapping("/short/urls")
    public ResponseEntity<?> openRealURL(@RequestParam("shortenUrlId") final String urlId) {
        final String originalUrl = this.urlShortenService.findOriginalURLByShortUrl(urlId);
        final URI location = URI.create(originalUrl);
        return ResponseEntity
                .status(HttpStatus.PERMANENT_REDIRECT)
                .location(location)
                .build();
    }
}
