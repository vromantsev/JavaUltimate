package com.bobocode.nasa.utils;

import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import static com.bobocode.nasa.service.ImageService.HTTP_PORT;
import static com.bobocode.nasa.utils.SocketUtils.createSocket;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;


public final class HttpUtils {

    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String LOCATION = "Location";

    private HttpUtils() {}

    /**
     * GET request template.
     *
     * @return request template as a string
     */
    public static String getPhotosRequest() {
        return """
                GET /mars-photos/api/v1/rovers/curiosity/photos?sol=16&api_key=CoWicfDGQaa3LIzyDeOq4tk4MG0heBeACNNXVgay HTTP/1.1
                Host: api.nasa.gov
                Content-Type: application/json
                Connection: close
                """;
    }

    /**
     * HEAD request template.
     *
     * @param endpoint an endpoint which is a data source
     * @param host host
     * @return request template as a string
     */
    public static String getHeadersRequest(final String endpoint, final String host) {
        return String.format("""
                HEAD %s HTTP/1.1
                Host: %s
                Connection: close
                """, endpoint, host);
    }

    /**
     * Helps to retrieve Content-Length header.
     *
     * @param location picture url, extracted from Location header
     * @return content length wrapped in {@link Optional}
     */
    public static Optional<Long> getContentLength(final URL location) {
        try (var socket = createSocket(location.getHost(), HTTP_PORT, false);
             var writer = new PrintStream(socket.getOutputStream())) {
            writer.println(getHeadersRequest(location.getPath(), location.getHost()));
            writer.flush();
            final Map<String, String> headers = HttpUtils.getHeaders(socket.getInputStream());
            return Optional.of(Long.parseLong(headers.get(CONTENT_LENGTH).trim()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helps to retrieve Location header.
     *
     * @param url original url
     * @return location wrapper in {@link Optional}
     */
    public static Optional<String> getLocation(final URL url) {
        try (var socket = createSocket(url.getHost(), HTTP_PORT, false);
             var writer = new PrintStream(socket.getOutputStream())) {
            writer.println(getHeadersRequest(url.getPath(), url.getHost()));
            writer.flush();
            final Map<String, String> headers = HttpUtils.getHeaders(socket.getInputStream());
            return Optional.ofNullable(headers.getOrDefault(LOCATION, null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helps to parse headers received in response.
     *
     * @param inputStream socket input stream
     * @return headers as a map
     */
    public static Map<String, String> getHeaders(final InputStream inputStream) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .filter(line -> line.contains(":"))
                    .filter(line -> !line.contains("{") && !line.contains("}"))
                    .collect(toMap(line -> line.split(":")[0], line -> line.split(":", 2)[1]));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helps to extract response body to a single string.
     *
     * @param inputStream socket input stream
     * @return response body
     */
    public static String getBody(final InputStream inputStream) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines()
                    .dropWhile(line -> !line.isBlank())
                    .filter(line -> line.contains("{") || line.contains("}"))
                    .collect(joining());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
