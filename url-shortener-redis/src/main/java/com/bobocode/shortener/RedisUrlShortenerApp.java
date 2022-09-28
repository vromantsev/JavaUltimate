package com.bobocode.shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisUrlShortenerApp {
    public static void main(String[] args) {
        SpringApplication.run(RedisUrlShortenerApp.class, args);
    }
}
