package com.bobocode.controller;

import com.bobocode.dto.PictureRequest;
import com.bobocode.service.MarsPictureService;
import com.bobocode.utils.HttpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mars/pictures")
public class MarsPictureController {

    private final MarsPictureService marsPictureService;

    @PostMapping("/largest")
    public ResponseEntity<?> generateCommandId(@RequestBody final PictureRequest request) {
        final String commandId = this.marsPictureService.generateCommandId(request);
        return ResponseEntity.ok()
                .location(HttpUtils.buildLocation(commandId))
                .build();
    }

    @GetMapping(value = "/largest/{commandId}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getLargestMarsPicture(@PathVariable("commandId") final String commandId) {
        final URI pictureUri = this.marsPictureService.getPictureUriByCommandId(commandId);
        final byte[] picture = this.marsPictureService.downloadPicture(pictureUri);
        return ResponseEntity.ok()
                .body(picture);
    }
}
