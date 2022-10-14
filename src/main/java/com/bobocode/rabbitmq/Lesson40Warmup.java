package com.bobocode.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Lesson40Warmup {

    private static final String HOST = "93.175.204.87";
    private static final int PORT = 5672;
    private static final String EXCHANGE = "participants-direct-exchange";

    public static void main(String[] args) {
        var cf = new ConnectionFactory();
        cf.setHost(HOST);
        cf.setPort(PORT);

        try (var connection = cf.newConnection();
             final Channel channel = connection.createChannel()) {
            channel.basicPublish(EXCHANGE, "", null, getBody().getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBody() {
        return """
                {
                    "firstName": "Vladyslav",
                    "lastName": "Romantsev"
                }
                """;
    }
}
