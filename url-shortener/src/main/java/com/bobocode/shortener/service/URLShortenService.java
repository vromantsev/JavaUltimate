package com.bobocode.shortener.service;

import com.bobocode.shortener.dto.URLPayload;

public interface URLShortenService {

    String shortenUrl(final URLPayload payload);

    String findOriginalURLByShortUrl(final String shortUrl);

}
