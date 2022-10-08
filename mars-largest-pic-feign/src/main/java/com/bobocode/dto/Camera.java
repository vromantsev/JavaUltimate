package com.bobocode.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Camera(Long id, String name, Long roverId, @JsonProperty("full_name") String fullName) {
}
