package com.bobocode.shortener.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "shortUrlId"})
@Builder
@ToString
@Entity
@Table(name = "shortened_urls")
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "short_url_gen")
    @SequenceGenerator(name = "short_url_seq", sequenceName = "short_url_gen", schema = "romantsev", allocationSize = 1)
    private Long id;

    @Column(name = "short_url_id", nullable = false)
    private String shortUrlId;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
