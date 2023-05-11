package com.example.springbatchexample.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "books")
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
//@NamedEntityGraph(name = "author-genre-graph",
//        attributeNodes = {@NamedAttributeNode("author"),
//                @NamedAttributeNode("genre")})
public class BookJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private final Long bookId;

    @Column(name = "book_name", length = 100)
    private final String bookName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "author_id", referencedColumnName = "author_id")
    private final AuthorJpa author;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "genre_id", referencedColumnName = "genre_id")
    private final GenreJpa genre;
}
