package com.bobocode.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitMQConfig {

    public static final String PICTURE_EXCHANGE = "pictures-exchange";
    public static final String PICTURE_COMMAND_QUEUE = "largest-picture-command-queue";

    @Bean
    public Queue largestPictureCommandQueue() {
        return new Queue(PICTURE_COMMAND_QUEUE);
    }

    @Bean
    public Exchange picturesExchange() {
        return new DirectExchange(PICTURE_EXCHANGE);
    }

    @Bean
    public Binding pictureQueueToPictureExchangeBinding(final Queue largestPictureCommandQueue,
                                                        final Exchange picturesExchange) {
        return BindingBuilder
                .bind(largestPictureCommandQueue)
                .to(picturesExchange)
                .with("")
                .noargs();
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
