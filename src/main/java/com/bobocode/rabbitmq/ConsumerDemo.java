package com.bobocode.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ConsumerDemo {

    private static final String HOST = "localhost";
    private static final int PORT = 5672;

    public static void main(String[] args) {
        var cf = new ConnectionFactory();
        cf.setHost(HOST);
        cf.setPort(PORT);

        try  {
            var connection = cf.newConnection();
            var channel = connection.createChannel();
            final AMQP.Queue.DeclareOk declareOk = channel.queueDeclare();
            channel.queueBind(declareOk.getQueue(), "announcement-fanout", "");
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                System.out.println("Consumed message: " + new String(message.getBody(), StandardCharsets.UTF_8));
            };
            CancelCallback cancelCallback = consumerTag -> {};
            channel.basicConsume(declareOk.getQueue(), deliverCallback, cancelCallback);
        } catch (IOException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
