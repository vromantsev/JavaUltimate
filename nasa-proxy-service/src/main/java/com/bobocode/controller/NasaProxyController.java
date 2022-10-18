package com.bobocode.controller;

import com.bobocode.entity.Picture;
import com.bobocode.service.NasaProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mars/pictures")
public class NasaProxyController {

    private final NasaProxyService nasaProxyService;

    @GetMapping(value = "/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> proxyCallToNasaService(@RequestParam("sol") final int sol,
                                                         @RequestParam(required = false, name = "camera") final String camera) {
        final Picture picture = this.nasaProxyService.proxyCallToNasa(sol, camera);
        return ResponseEntity.status(HttpStatus.OK)
                .header("x-nasa-largest-picture-id", picture.getId())
                .build();
    }

    @GetMapping(value = "/redis/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getPictureFromRedis(@RequestParam("id") final String id) {
        byte[] picture = this.nasaProxyService.getPictureFromCacheById(id);
        return ResponseEntity.ok(picture);
    }
}
