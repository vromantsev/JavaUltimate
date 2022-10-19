package com.bobocode.controller;

import com.bobocode.client.NasaPictureFeignClient;
import com.bobocode.dto.Photos;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mars/pictures")
@RequiredArgsConstructor
public class NasaProxyController {

    @Value("${nasa.api-key}")
    private String apiKey;

    private final NasaPictureFeignClient nasaPictureFeignClient;

    @GetMapping("/all")
    public ResponseEntity<Photos> getRoverPhotos(@RequestParam final int sol,
                                                 @RequestParam(required = false) final String camera) {
        final Photos photos = this.nasaPictureFeignClient.getMarsRoverPhotos(sol, this.apiKey, camera);
        return ResponseEntity.ok(photos);
    }
}
