package com.bobocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class EventDrivenMarsPictureServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventDrivenMarsPictureServiceApplication.class, args);
    }
}
