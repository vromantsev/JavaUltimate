package com.bobocode.rabbitmq.lesson39;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class BindedConsumerDemo {

    private static final String HOST = "localhost";
    private static final int PORT = 5672;

    public static void main(String[] args) {
        var cf = new ConnectionFactory();
        cf.setHost(HOST);
        cf.setPort(PORT);

        try {
            final Connection connection = cf.newConnection();
            final Channel channel = connection.createChannel();

            final String queue = "vromantsev-queue";
            final AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(queue, false, false, false, Collections.emptyMap());

            channel.queueBind(queue, "announcements-topic", "petros.*");

            DeliverCallback callback = (consumerTag, message) -> {
                System.out.println("Received message: " + new String(message.getBody(), StandardCharsets.UTF_8));
            };
            CancelCallback cancelCallback = consumerTag -> {};
            channel.basicConsume(queue, callback, cancelCallback);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
