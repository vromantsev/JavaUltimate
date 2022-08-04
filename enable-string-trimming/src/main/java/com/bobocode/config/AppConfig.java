package com.bobocode.config;

import com.magic.trimming.annotation.EnableStringTrimming;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = "com.bobocode")
@Configuration
@EnableStringTrimming
public class AppConfig {
}
