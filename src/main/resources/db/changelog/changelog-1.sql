--liquibase formatted sql

--changeset westermeister:1
CREATE TABLE user_ (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name CHARACTER VARYING(255),
    email CHARACTER VARYING(255) UNIQUE,
    password CHARACTER VARYING(255),
    role CHARACTER VARYING(255),
    created TIMESTAMP(0) WITHOUT TIME ZONE,
    last_sign_in TIMESTAMP(0) WITHOUT TIME ZONE
);

--changeset westermeister:2
CREATE TABLE snippet (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    slot SMALLINT,
    summary CHARACTER VARYING(255),
    source CHARACTER VARYING(255),
    last_updated TIMESTAMP(0) WITHOUT TIME ZONE
);

