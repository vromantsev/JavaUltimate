package com.bobocode.reactive.service;

import reactor.core.publisher.Mono;

public interface PictureService {

    Mono<byte[]> getNasaLargestPicture(final int sol);

}
