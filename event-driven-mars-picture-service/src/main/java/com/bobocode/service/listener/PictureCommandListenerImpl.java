package com.bobocode.service.listener;

import com.bobocode.config.RabbitMQConfig;
import com.bobocode.dto.PicData;
import com.bobocode.dto.PictureCommandDetails;
import com.bobocode.repository.PictureRepository;
import com.bobocode.service.MarsPictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component("pictureCommandListener")
public class PictureCommandListenerImpl implements PictureCommandListener {

    private final PictureRepository pictureRepository;
    private final MarsPictureService marsPictureService;

    @RabbitListener(queues = RabbitMQConfig.PICTURE_COMMAND_QUEUE)
    @Override
    public void onMessage(final PictureCommandDetails details) {
        log.info("Received: {}", details);
        final PicData largestMarsPicture = this.marsPictureService.getLargestMarsPicture(details.request());
        final String commandId = details.commandId();
        final String url = largestMarsPicture.url();
        log.info("Saving picture: commandId='{}'m url='{}'", commandId, url);
        this.pictureRepository.save(commandId, url);
    }
}
