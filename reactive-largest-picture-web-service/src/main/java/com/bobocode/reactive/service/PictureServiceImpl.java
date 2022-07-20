package com.bobocode.reactive.service;

import com.bobocode.reactive.config.AppProperties;
import com.bobocode.reactive.domain.PicData;
import com.bobocode.reactive.domain.Pictures;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String SOL = "sol";
    private static final String API_KEY = "api_key";

    private final WebClient webClient;
    private final AppProperties appProperties;

    public PictureServiceImpl(final WebClient webClient, final AppProperties appProperties) {
        this.webClient = webClient;
        this.appProperties = appProperties;
    }

    @Override
    public Mono<byte[]> getNasaLargestPicture(final int sol) {
        var photosUrl = getPhotosUrl(sol);
        return this.webClient.get()
                .uri(photosUrl)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Pictures.class))
                .flatMapMany(pictures -> Flux.fromIterable(pictures.photos()))
                .flatMap(pic -> this.webClient.head()
                        .uri(pic.imgSrc())
                        .exchangeToMono(ClientResponse::toBodilessEntity)
                        .map(HttpEntity::getHeaders)
                        .mapNotNull(HttpHeaders::getLocation)
                        .map(URI::toString)
                        .flatMap(redirectUrl -> this.webClient.head()
                                .uri(redirectUrl)
                                .exchangeToMono(ClientResponse::toBodilessEntity)
                                .map(HttpEntity::getHeaders)
                                .map(HttpHeaders::getContentLength)
                                .map(contentLength -> new PicData(redirectUrl, contentLength))
                        )
                )
                .reduce((pic1, pic2) -> pic1.size() > pic2.size() ? pic1 : pic2)
                .map(PicData::url)
                .flatMap(url -> WebClient.create(url)
                        .mutate()
                        .codecs(config -> config.defaultCodecs().maxInMemorySize(10 * 100_000))
                        .build()
                        .get()
                        .uri(url)
                        .exchangeToMono(clientResponse -> clientResponse.bodyToMono(byte[].class)));
    }

    private String getPhotosUrl(final int sol) {
        return UriComponentsBuilder.fromUriString(this.appProperties.getNasaApiUrl())
                .queryParam(SOL, sol)
                .queryParam(API_KEY, this.appProperties.getApiKey())
                .build()
                .toUriString();
    }
}
