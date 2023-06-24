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
    summary CHARACTER VARYING(1000),
    source CHARACTER VARYING(1000),
    last_updated TIMESTAMP(0) WITHOUT TIME ZONE
);

--changeset westermeister:3
CREATE TABLE lock (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    task CHARACTER VARYING(255) UNIQUE,
    created TIMESTAMP(0) WITHOUT TIME ZONE
);
