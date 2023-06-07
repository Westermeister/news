--liquibase formatted sql

--changeset westermeister:1
CREATE TABLE user_ (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    created BIGINT,
    last_sign_in BIGINT
);
