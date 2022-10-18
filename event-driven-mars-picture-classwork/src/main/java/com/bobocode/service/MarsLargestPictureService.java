package com.bobocode.service;

import com.bobocode.dto.PicData;

public interface MarsLargestPictureService {

    PicData findLargestMarsPicture(final int sol, final String camera);

}
