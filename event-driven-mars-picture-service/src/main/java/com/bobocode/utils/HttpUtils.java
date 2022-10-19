package com.bobocode.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@UtilityClass
public class HttpUtils {

    public URI buildLocation(final String commandId) {
        return URI.create(ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment(commandId)
                .toUriString());
    }
}
