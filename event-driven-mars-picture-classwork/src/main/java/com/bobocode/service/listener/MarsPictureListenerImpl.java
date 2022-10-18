package com.bobocode.service.listener;

import com.bobocode.dto.PicData;
import com.bobocode.dto.PictureRequest;
import com.bobocode.service.MarsLargestPictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarsPictureListenerImpl implements MarsPictureListener {

    private final MarsLargestPictureService marsLargestPictureService;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "picture-request-fanout")
    @Override
    public void onMessage(PictureRequest request) {
        final PicData largestMarsPicture = this.marsLargestPictureService.findLargestMarsPicture(request.sol(), request.camera());
        var response = new PictureResponse(
                new User("Vladyslav", "Romantsev"),
                request,
                new Picture(largestMarsPicture.url())
        );
        this.rabbitTemplate.convertAndSend("", "picture-result-queue", response);
    }

    record PictureResponse(User user, PictureRequest request, Picture picture) {}

    record User(String firstName, String lastName) {}


    record Picture(String url) {}
}
