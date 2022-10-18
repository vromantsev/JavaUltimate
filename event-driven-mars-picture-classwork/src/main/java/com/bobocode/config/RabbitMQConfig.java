package com.bobocode.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {

    private final AppProperties appProperties;

    @Bean
    public Queue pictureRequestQueue() {
        return QueueBuilder
                .nonDurable(this.appProperties.getQueueName())
                .autoDelete()
                .build();
    }

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange(this.appProperties.getFanoutBinding()).suppressDeclaration().build();
    }

    @Bean
    public Binding pictureRequestFanoutBinding() {
        return BindingBuilder
                .bind(pictureRequestQueue())
                .to(exchange())
                .with("")
                .noargs();
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
