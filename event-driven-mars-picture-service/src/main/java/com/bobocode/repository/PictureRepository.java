package com.bobocode.repository;

import java.net.URI;

public interface PictureRepository {

    void save(final String commandId, final String pictureUrl);

    URI getPicture(final String commandId);

}
