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

--changeset westermeister:2
CREATE TABLE session (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id BIGINT,
    token VARCHAR(255),
    created BIGINT,
    expires BIGINT,
    CONSTRAINT fk__session__user_id__user FOREIGN KEY (user_id) REFERENCES user_(id) ON DELETE CASCADE
);
