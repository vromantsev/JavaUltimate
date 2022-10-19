package com.bobocode.service;

import com.bobocode.config.RabbitMQConfig;
import com.bobocode.dto.PictureCommandDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(final PictureCommandDetails pictureCommandDetails) {
        Objects.requireNonNull(pictureCommandDetails, "Parameter [pictureCommandDetails] must not be null!");
        log.info("Received request '{}', sending to '{}'", pictureCommandDetails, RabbitMQConfig.PICTURE_COMMAND_QUEUE);
        this.rabbitTemplate.convertAndSend("", RabbitMQConfig.PICTURE_COMMAND_QUEUE, pictureCommandDetails);
    }
}
