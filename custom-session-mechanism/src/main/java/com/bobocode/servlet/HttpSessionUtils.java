package com.bobocode.servlet;

import jakarta.servlet.http.Cookie;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Utility class that helps to manage http session.
 */
public final class HttpSessionUtils {

    static final String SESSION_ID = "SESSION_ID";

    private static final ConcurrentMap<String, Map<String, String>> SESSION_MAP = new ConcurrentHashMap<>();

    private HttpSessionUtils() {}

    /**
     * Extracts sessionId from cookies or generates a new one if an id is missing.
     *
     * @param cookies cookies
     * @return sessionId
     */
    public static String getSessionId(final Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equalsIgnoreCase(SESSION_ID))
                .findFirst()
                .map(Cookie::getValue)
                .orElseGet(() -> UUID.randomUUID().toString());
    }

    /**
     * Retrieves an attribute by a given parameter name associated with a given sessionId.
     *
     * @param sessionId current sessionId
     * @param parameter parameter name
     * @return attribute value associated with the given session
     */
    public static Optional<String> getAttribute(final String sessionId, final String parameter) {
        Objects.requireNonNull(sessionId);
        return Optional.ofNullable(SESSION_MAP.get(sessionId)).map(session -> session.get(parameter));
    }

    /**
     * Sets an attribute for a given sessionId. If session exists, attribute value is replaced.
     *
     * @param sessionId current sessionId
     * @param parameter parameter name to set
     * @param value attribute value
     */
    public static void setAttribute(final String sessionId, final String parameter, final String value) {
        Objects.requireNonNull(sessionId);
        Objects.requireNonNull(parameter);
        Objects.requireNonNull(value);
        final Map<String, String> params = new HashMap<>();
        params.put(parameter, value);
        SESSION_MAP.put(sessionId, params);
    }
}
