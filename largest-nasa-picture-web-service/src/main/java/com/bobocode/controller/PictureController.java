package com.bobocode.controller;

import com.bobocode.domain.PicData;
import com.bobocode.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bobocode.utils.HttpUtils.uri;

@RestController
@RequestMapping("/pictures")
public class PictureController {

    private final PictureService pictureService;

    @Autowired
    public PictureController(final PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping("/{sol}/largest")
    public ResponseEntity<?> getLargestNASAPicture(@PathVariable("sol") final String sol) {
        final String largestPictureUrl = this.pictureService.getLargestPicture(sol);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(uri(largestPictureUrl))
                .build();
    }
}
