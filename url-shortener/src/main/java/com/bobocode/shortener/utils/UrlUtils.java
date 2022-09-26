package com.bobocode.shortener.utils;

import lombok.experimental.UtilityClass;

import java.net.URI;

@UtilityClass
public class UrlUtils {

    public URI createURI(final String template) {
        return URI.create(template);
    }
}
