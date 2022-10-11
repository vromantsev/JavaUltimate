package com.bobocode.client;

import com.bobocode.dto.Photos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("${nasa.photos.source}")
public interface MarsPictureFeignClient {

    @GetMapping("${nasa.photos.endpoint}")
    Photos getMarsRoverPhotos(@RequestParam("sol") final int sol,
                              @RequestParam("api_key") final String apiKey,
                              @RequestParam(required = false) final String camera);
}
