package com.bobocode.reactive.marslargestpic.service;

import reactor.core.publisher.Mono;

public interface MarsLargestPictureService {

    Mono<byte[]> getLargestMarsPicture(final int sol, final String camera);

}
