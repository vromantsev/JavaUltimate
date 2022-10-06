package com.bobocode.reactive.marslargestpic.controller;

import com.bobocode.reactive.marslargestpic.service.MarsLargestPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/mars/pictures")
@RequiredArgsConstructor
public class MarsPictureController {

    private final MarsLargestPictureService marsLargestPictureService;

    @GetMapping(value = "/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public Mono<byte[]> getMarsLargestPicture(@RequestParam("sol") final int sol,
                                              @RequestParam(name = "camera", required = false) final String camera) {
        return this.marsLargestPictureService.getLargestMarsPicture(sol, camera);
    }
}
