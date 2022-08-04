package com.bobocode.reactive.controller;

import com.bobocode.reactive.service.PictureService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/pictures")
public class NasaPictureController {

    private final PictureService pictureService;

    public NasaPictureController(final PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping(value = "/{sol}/largest", produces = MediaType.IMAGE_PNG_VALUE)
    public Mono<byte[]> getLargestNasaPicture(@PathVariable("sol") final int sol) {
        return this.pictureService.getNasaLargestPicture(sol);
    }
}
