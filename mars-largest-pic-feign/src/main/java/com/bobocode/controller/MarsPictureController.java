package com.bobocode.controller;

import com.bobocode.service.MarsPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mars/pictures")
public class MarsPictureController {

    private final MarsPictureService marsPictureService;

    @GetMapping(value = "/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getMarsLargestPicture(@RequestParam("sol") final int sol,
                                                        @RequestParam(value = "camera", required = false) final String camera) {
        final byte[] largestMarsPicture = this.marsPictureService.getLargestMarsPicture(sol, camera);
        return ResponseEntity.ok(largestMarsPicture);
    }
}
