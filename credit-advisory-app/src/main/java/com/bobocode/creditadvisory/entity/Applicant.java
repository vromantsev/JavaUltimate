package com.bobocode.creditadvisory.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "applicant")
public class Applicant extends SystemUser {

    @Column(name = "social_security_number", nullable = false)
    private String socialSecurityNumber;

    @Embedded
    private Address address;

    @ElementCollection(targetClass = PhoneNumber.class)
    @CollectionTable(name = "phone_number", joinColumns = @JoinColumn(name = "applicant_id"))
    private Set<PhoneNumber> phoneNumbers = new HashSet<>();

    @OneToMany(mappedBy = "applicant", fetch = FetchType.LAZY)
    private List<Application> applications = new ArrayList<>();
}
