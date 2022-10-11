package com.bobocode.service;

import com.bobocode.client.MarsPictureFeignClient;
import com.bobocode.dto.Photos;
import com.bobocode.dto.PicData;
import com.bobocode.exception.PictureNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Comparator;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MarsPictureServiceImpl implements MarsPictureService {

    @Value("${nasa.api-key}")
    private String apiKey;

    private final  MarsPictureFeignClient marsPictureFeignClient;
    private final RestTemplate restTemplate;

    @Cacheable("mars-largest-pic")
    @Override
    public byte[] getMarsLargestPicture(final int sol, final String camera) {
        final Photos roverPhotos = this.marsPictureFeignClient.getMarsRoverPhotos(sol, apiKey, camera);
        return Objects.requireNonNull(roverPhotos).photos().parallelStream()
                .map(photo -> {
                    final HttpHeaders originalHeaders = this.restTemplate.headForHeaders(photo.imgSrc());
                    final URI location = originalHeaders.getLocation();
                    final HttpHeaders locationHeaders = this.restTemplate.headForHeaders(Objects.requireNonNull(location));
                    final long contentLength = locationHeaders.getContentLength();
                    return new PicData(location.toString(), contentLength);
                })
                .max(Comparator.comparing(PicData::contentLength))
                .map(picData -> this.restTemplate.getForObject(picData.url(), byte[].class))
                .orElseThrow(() -> new PictureNotFoundException("No pictures found"));
    }
}
