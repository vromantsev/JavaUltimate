package com.bobocode.shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class UrlShortenApp {
    public static void main(String[] args) {
        SpringApplication.run(UrlShortenApp.class, args);
    }
}
