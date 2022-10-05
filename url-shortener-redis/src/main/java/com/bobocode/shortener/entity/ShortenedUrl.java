package com.bobocode.shortener.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Builder
@ToString
@RedisHash(value = "shortened-url")
public class ShortenedUrl {
    private String id;
    private String originalUrl;
    private String title;
    private LocalDateTime createdAt;
}
