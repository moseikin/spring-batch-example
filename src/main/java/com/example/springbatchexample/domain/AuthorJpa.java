package com.example.springbatchexample.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@Entity
@Table(name = "authors")
@NoArgsConstructor(force = true)
public class AuthorJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private final Long id;

    @Column(name = "author_surname", length = 100)
    private final String surname;

    @Column(name = "author_name", length = 100)
    private final String name;
}
