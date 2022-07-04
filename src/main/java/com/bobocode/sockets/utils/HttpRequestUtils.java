package com.bobocode.sockets.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

import static java.nio.charset.Charset.defaultCharset;

public final class HttpRequestUtils {

    private static final String GET = "GET";
    private static final String HTTP_PROTOCOL = "HTTP/1.1";
    private static final String DEFAULT_NAME = "Bro";
    private static final String HELLO_ENDPOINT = "/hello";

    private HttpRequestUtils() {}

    public static String getResponse(final String requestBody) {
        Objects.requireNonNull(requestBody);
        String name = null;
        if (requestBody.startsWith(GET) && requestBody.endsWith(HTTP_PROTOCOL)) {
            final String[] lexems = requestBody.split(" ");
            name = Arrays.stream(lexems)
                    .filter(path -> path.contains(HELLO_ENDPOINT))
                    .map(path -> path.split("=")[1])
                    .findFirst()
                    .orElse(DEFAULT_NAME);
        }
        return name;
    }

    public static void processRequest(Socket socket) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             final PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, defaultCharset())) {
            System.out.println("Accepted a request from " + socket.getRemoteSocketAddress());
            final String requestBody = reader.readLine();
            String name = HttpRequestUtils.getResponse(requestBody);
            final String message = String.format("<h2>Hello, %s!</h2>", name);
            var response = String.format("""
                    HTTP/1.1 200 OK
                    Date: %s
                    Cache-Control: no-cache
                    Content-Type: text/html
                    Content-Length: %d
                                        
                    %s
                    """, ZonedDateTime.now(), message.length(), message);
            writer.println(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
