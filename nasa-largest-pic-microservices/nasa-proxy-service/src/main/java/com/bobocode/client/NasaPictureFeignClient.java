package com.bobocode.client;

import com.bobocode.dto.Photos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "${spring.application.name}",
        url = "${nasa.base-url}"
)
public interface NasaPictureFeignClient {

    @GetMapping("${nasa.photos.endpoint}")
    Photos getMarsRoverPhotos(@RequestParam final int sol,
                              @RequestParam(value = "api_key") final String apiKey,
                              @RequestParam(required = false) final String camera);

}
