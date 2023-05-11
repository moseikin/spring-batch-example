package com.example.springbatchexample.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "authors")
public class Author {

    @Id
    private String id;

    @Field(name = "surname")
    private String surname;

    @Field(name = "name")
    private String name;
}
