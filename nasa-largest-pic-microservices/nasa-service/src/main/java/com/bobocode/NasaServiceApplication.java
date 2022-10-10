package com.bobocode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class NasaServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(NasaServiceApplication.class, args);
    }
}
