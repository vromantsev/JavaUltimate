package com.bobocode.reactive.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Picture(@JsonProperty("img_src") String imgSrc) {
}
