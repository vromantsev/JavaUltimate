package com.bobocode.service;

import com.bobocode.dto.PicData;
import com.bobocode.dto.PictureRequest;

import java.net.URI;

public interface MarsPictureService {

    String generateCommandId(final PictureRequest request);

    URI getPictureUriByCommandId(final String commandId);

    byte[] downloadPicture(final URI url);

    PicData getLargestMarsPicture(final PictureRequest request);
}
