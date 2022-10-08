package com.bobocode.client;

import com.bobocode.dto.Photos;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        value = "${app.feign-client.name}",
        url = "${app.feign-client.host}"
)
public interface MarsPhotosFeignClient {

    @GetMapping(value = "${app.feign-client.url}")
    Photos getPhotos(@RequestParam("sol") final int sol,
                     @RequestParam(value = "api_key") final String apiKey,
                     @RequestParam(value = "camera", required = false) final String camera);
}
