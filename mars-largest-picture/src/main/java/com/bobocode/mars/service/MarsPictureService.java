package com.bobocode.mars.service;

import com.bobocode.mars.dto.PicData;

public interface MarsPictureService {

    PicData getLargestMarsPicture(final int sol, final String camera);

}
