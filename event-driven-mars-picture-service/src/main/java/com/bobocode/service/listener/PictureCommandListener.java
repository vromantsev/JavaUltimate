package com.bobocode.service.listener;

import com.bobocode.dto.PictureCommandDetails;

public interface PictureCommandListener {

    void onMessage(final PictureCommandDetails details);

}
