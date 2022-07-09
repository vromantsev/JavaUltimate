package com.bobocode.nasa.service;

import com.bobocode.nasa.dto.ImgData;
import com.bobocode.nasa.dto.Photo;
import com.bobocode.nasa.dto.Photos;
import com.bobocode.nasa.utils.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.bobocode.nasa.utils.HttpUtils.*;
import static com.bobocode.nasa.utils.SocketUtils.createSocket;

/**
 * Allows to find a photo of the largest size from a NASA public API.
 */
public class ImageService {

    private static final String SSL_HOST = "api.nasa.gov";
    private static final int SSL_PORT = 443;
    public static final int HTTP_PORT = 80;

    private final ObjectMapper mapper;

    {
        mapper = new ObjectMapper();
    }

    /**
     * Encapsulates all the logic within.
     */
    public void findPictureOfLargestSize() {
        try (final SSLSocket clientSocket = (SSLSocket) createSocket(SSL_HOST, SSL_PORT, true);
             final PrintWriter writer = new PrintWriter(clientSocket.getOutputStream())) {
            sendRequest(writer);
            var originalUrls = getImageUrls(clientSocket.getInputStream());
            processResponse(originalUrls);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends initial request from a socket.
     */
    private void sendRequest(final PrintWriter writer) {
        writer.println(getPhotosRequest());
        writer.flush();
    }

    /**
     * Helps to find a photo with the max size and print its info.
     *
     * @param urls a list of photo urls
     */
    private void processResponse(final List<URL> urls) {
        urls.parallelStream()
                .map(originalUrl -> {
                    final String location = getLocation(originalUrl).orElseThrow();
                    final Long contentLength = getContentLength(url(location)).orElseThrow();
                    return new ImgData(originalUrl.toString(), contentLength);
                })
                .max(Comparator.comparing(ImgData::size))
                .ifPresent(data -> System.out.println("originalUrl: " + data.originalUrl() + ", size: " + data.size()));
    }

    /**
     * Extracts image urls from an {@link InputStream}.
     *
     * @param inputStream socket {@link InputStream}
     * @return list of image urls
     */
    private List<URL> getImageUrls(final InputStream inputStream) {
        try {
            final String body = HttpUtils.getBody(inputStream);
            final Photos photos = this.mapper.readValue(body, Photos.class);
            return photos.photos().stream()
                    .map(Photo::imgSrc)
                    .map(this::url)
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs {@link URL} object.
     */
    private URL url(final String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
