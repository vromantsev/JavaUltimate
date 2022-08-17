package com.bobocode.jdbc;

import java.time.LocalDateTime;

public record Product(Long id, String name, double price, LocalDateTime createdAt) {
}
