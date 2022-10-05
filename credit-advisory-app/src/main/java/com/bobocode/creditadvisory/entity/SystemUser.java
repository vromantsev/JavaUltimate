package com.bobocode.creditadvisory.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"username"})
@MappedSuperclass
public class SystemUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_user_id_gen")
    @SequenceGenerator(name = "system_user_id_gen", sequenceName = "system_user_seq", allocationSize = 1)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

}
