package com.bobocode.marslargestpictureredisfeignapp.service;

import com.bobocode.marslargestpictureredisfeignapp.client.MarsProxyFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NasaPictureServiceImpl implements NasaPictureService {

    private final MarsProxyFeignClient marsProxyFeignClient;

    @Cacheable("mars-largest-picture")
    @Override
    public byte[] getLargestPicture(final int sol, final String camera) {
        return this.marsProxyFeignClient.getMarsLargestPicture(sol, camera);
    }
}
