package com.bobocode.service;

import com.bobocode.entity.Picture;

public interface NasaProxyService {

    Picture proxyCallToNasa(final int sol, final String camera);

    byte[] getPictureFromCacheById(final String id);

}
