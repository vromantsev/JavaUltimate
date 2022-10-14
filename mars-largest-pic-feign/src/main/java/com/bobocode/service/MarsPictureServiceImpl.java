package com.bobocode.service;

import com.bobocode.client.MarsPhotosFeignClient;
import com.bobocode.config.AppProperties;
import com.bobocode.dto.Photo;
import com.bobocode.dto.Photos;
import com.bobocode.dto.PicData;
import com.bobocode.exception.PictureNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class MarsPictureServiceImpl implements MarsPictureService {

    private final MarsPhotosFeignClient marsPhotosFeignClient;
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    @Cacheable("mars-largest-pic")
    @Override
    public byte[] getLargestMarsPicture(int sol, String camera) {
        final Photos photos = this.marsPhotosFeignClient.getPhotos(sol, appProperties.getApiKey(), camera);
        return Objects.requireNonNull(photos).photos()
                .parallelStream()
                .map(getPicDataFunction())
                .max(Comparator.comparing(PicData::contentLength))
                .map(picData -> this.restTemplate.getForObject(picData.url(), byte[].class))
                .orElseThrow(() -> new PictureNotFoundException("No pictures found!"));
    }

    private Function<Photo, PicData> getPicDataFunction() {
        return photo -> {
            final String originalUrl = photo.imgSrc();
            final HttpHeaders originalHeaders = this.restTemplate.headForHeaders(originalUrl);
            final long contentLength = originalHeaders.getContentLength();
            return new PicData(originalUrl, contentLength);
        };
    }
}
