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

@Entity
@Table(name = "genres")
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class GenreJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id")
    private final Long id;

    @Column(name = "genre_name", length = 100)
    private final String name;
}
