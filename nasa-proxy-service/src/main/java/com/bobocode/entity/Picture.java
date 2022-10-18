package com.bobocode.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@RedisHash(value = "nasa-largest-picture", timeToLive = 60 * 60)
public class Picture {
    private String id;
    private byte[] content;
}
