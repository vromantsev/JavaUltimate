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
@RequestMapping("/mars/pictures")
@RequiredArgsConstructor
public class MarsPictureController {

    private final MarsPictureService marsPictureService;

    @GetMapping(value = "/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getLargestMarsPicture(@RequestParam("sol") final int sol,
                                                        @RequestParam(value = "camera", required = false) final String camera) {
        var picture = this.marsPictureService.getMarsLargestPicture(sol, camera);
        return ResponseEntity.ok(picture);
    }
}
