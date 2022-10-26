package com.bobocode.marslargestpictureredisfeignapp.controller;

import com.bobocode.marslargestpictureredisfeignapp.service.NasaPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mars/pictures")
@RequiredArgsConstructor
public class MarsProxyController {

    private final NasaPictureService nasaPictureService;

    @GetMapping(value = "/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getMarsLargestPicture(@RequestParam("sol") final int sol,
                                                        @RequestParam(required = false, value = "camera") final String camera) {
        final byte[] marsLargestPicture = this.nasaPictureService.getLargestPicture(sol, camera);
        return ResponseEntity.ok(marsLargestPicture);
    }
}
