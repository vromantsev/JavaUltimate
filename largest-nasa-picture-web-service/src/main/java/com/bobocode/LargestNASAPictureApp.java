package com.bobocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bobocode")
public class LargestNASAPictureApp {
    public static void main(String[] args) {
        SpringApplication.run(LargestNASAPictureApp.class, args);
    }
}
