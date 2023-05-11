create table if not exists genres
(
    genre_id   bigserial primary key,
    genre_name varchar(100)
);

create table if not exists authors
(
    author_id      bigserial primary key,
    author_surname varchar(100),
    author_name    varchar(100)
);

create table if not exists books
(
    book_id   bigserial primary key,
    book_name varchar(100),
    author_id bigint references authors (author_id),
    genre_id  bigint references genres (genre_id)
);

create table if not exists comments
(
    comment_id bigserial primary key,
    book_id    bigserial references books (book_id) on delete cascade,
    content    varchar(10000)
);

create table if not exists roles
(
    role varchar(30) primary key,
    description varchar
);

create table if not exists users
(
    login    varchar(10) primary key,
    password varchar not null,
    role varchar references roles (role)
);
