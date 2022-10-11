package com.bobocode.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ProducerDemo {

    private static final String HOST = "localhost";
    private static final int PORT = 5672;

    public static void main(String[] args) {
        var cf = new ConnectionFactory();
        cf.setHost(HOST);
        cf.setPort(PORT);
        try (var connection = cf.newConnection();
             var channel = connection.createChannel()) {
            channel.basicPublish("", "participants-queue", null, "Vladyslav Romantsev".getBytes(StandardCharsets.UTF_8));
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
