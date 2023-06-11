--liquibase formatted sql

--changeset westermeister:1
CREATE TABLE user_ (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name CHARACTER VARYING(255),
    email CHARACTER VARYING(255) UNIQUE,
    password CHARACTER VARYING(255),
    role CHARACTER VARYING(255),
    created TIMESTAMP(0) WITHOUT TIME ZONE,
    last_sign_in TIMESTAMP(0) WITHOUT TIME ZONE,
    failed_sign_in_buffer SMALLINT,
    next_allowed_sign_in TIMESTAMP(0) WITHOUT TIME ZONE
);
