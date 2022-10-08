package com.bobocode.reactive.marslargestpic.service;

import com.bobocode.reactive.marslargestpic.config.AppProperties;
import com.bobocode.reactive.marslargestpic.dto.Photos;
import com.bobocode.reactive.marslargestpic.dto.PicData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MarsLargestPictureServiceImpl implements MarsLargestPictureService {

    private final WebClient webClient;
    private final AppProperties appProperties;

    @Override
    public Mono<byte[]> getLargestMarsPicture(final int sol, final String camera) {
        final String apiUrl = buildApiUrl(sol, camera);
        return this.webClient.get()
                .uri(URI.create(apiUrl))
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Photos.class))
                .flatMapMany(photos -> Flux.fromIterable(photos.photos()))
                .flatMap(photo -> this.webClient.head()
                        .uri(photo.imgSrc())
                        .exchangeToMono(ClientResponse::toBodilessEntity)
                        .map(HttpEntity::getHeaders)
                        .mapNotNull(HttpHeaders::getLocation)
                        .flatMap(redirectUrl -> this.webClient.head()
                                .uri(redirectUrl)
                                .exchangeToMono(ClientResponse::toBodilessEntity)
                                .map(HttpEntity::getHeaders)
                                .map(headers -> new PicData(redirectUrl.toString(), headers.getContentLength()))
                        )
                )
                .reduce((picData1, picData2) -> picData1.contentLength() > picData2.contentLength() ? picData1 : picData2)
                .map(PicData::url)
                .flatMap(url -> WebClient.create(url)
                        .mutate()
                        .codecs(config -> config.defaultCodecs().maxInMemorySize(10 * 1000_000))
                        .build()
                        .get()
                        .uri(url)
                        .exchangeToMono(clientResponse -> clientResponse.bodyToMono(byte[].class))
                );
    }

    private String buildApiUrl(final int sol, final String camera) {
        return UriComponentsBuilder.fromUriString(this.appProperties.getApiUrl())
                .queryParam(this.appProperties.getSolHeader(), sol)
                .queryParam(this.appProperties.getApiKeyHeader(), this.appProperties.getApiKey())
                .queryParamIfPresent(this.appProperties.getCameraHeader(), Optional.ofNullable(camera))
                .build()
                .toUriString();
    }
}
