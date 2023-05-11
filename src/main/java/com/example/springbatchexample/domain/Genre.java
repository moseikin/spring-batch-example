package com.example.springbatchexample.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "genres")
public class Genre {

    @Id
    private String id;

    @Field(name = "name")
    private String name;
}
