package com.bobocode.utils;

import java.net.URI;
import java.net.URISyntaxException;

public final class HttpUtils {

    private HttpUtils() {}

    public static URI uri(final String originalUrl) {
        try {
            return new URI(originalUrl);
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
