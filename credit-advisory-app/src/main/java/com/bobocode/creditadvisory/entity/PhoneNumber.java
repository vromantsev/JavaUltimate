package com.bobocode.creditadvisory.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Embeddable
public class PhoneNumber {

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "phone")
    private String phone;

    public enum Type {
        HOME, WORK, MOBILE;
    }
}
