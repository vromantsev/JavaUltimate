package com.bobocode.mars;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class LargestMarsPictureApp {
    public static void main(String[] args) {
        SpringApplication.run(LargestMarsPictureApp.class, args);
    }
}
