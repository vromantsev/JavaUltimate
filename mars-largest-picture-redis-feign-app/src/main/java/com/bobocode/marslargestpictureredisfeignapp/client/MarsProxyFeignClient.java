package com.bobocode.marslargestpictureredisfeignapp.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "${spring.application.name}", url = "${app.nasa.url}")
public interface MarsProxyFeignClient {

    @GetMapping(value = "${app.nasa.endpoint}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getMarsLargestPicture(@RequestParam("sol") final int sol,
                                 @RequestParam(required = false, value = "camera") final String camera);
}
