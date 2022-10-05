package com.bobocode.mars.controller;

import com.bobocode.mars.dto.PicData;
import com.bobocode.mars.service.MarsPictureService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mars/pictures")
public class MarsLargestPictureController {

    private final MarsPictureService marsPictureService;

    public MarsLargestPictureController(MarsPictureService marsPictureService) {
        this.marsPictureService = marsPictureService;
    }

    @GetMapping(value = "/largest/{sol}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> findLargestMarsPicture(@PathVariable("sol") final int sol,
                                                         @RequestParam(required = false, name = "camera") final String camera) {
        final PicData largestMarsPicture = this.marsPictureService.getLargestMarsPicture(sol, camera);
        return ResponseEntity.ok(largestMarsPicture.photo());
    }
}
