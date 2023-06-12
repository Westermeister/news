# User

## Schema

```sql
CREATE TABLE user_ (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name CHARACTER VARYING(255),
    email CHARACTER VARYING(255) UNIQUE,
    password CHARACTER VARYING(255),
    role CHARACTER VARYING(255),
    created TIMESTAMP(0) WITHOUT TIME ZONE,
    last_sign_in TIMESTAMP(0) WITHOUT TIME ZONE,
);
```

## Columns

- id
    - Surrogate primary key
- name
    - User's real-life name (not a username)
- email
    - User's email address; 255 chars should be enough
    - Source: https://stackoverflow.com/a/574698
- password
    - This should be a salted hash, not plain text
- role
    - Used by Spring Security for authority / permissions
    - e.g. `ROLE_USER`, `ROLE_ADMIN`, etc.
- created
    - UNIX timestamp; precise to the second
    - Represents time that user first signed up / was added to the database
- last_sign_in
    - Same as above, but represents time that user last signed in
    - Signing up counts as signing in
