# Lock

## Schema

```sql
CREATE TABLE lock (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    task CHARACTER VARYING(255) UNIQUE,
    created TIMESTAMP(0) WITHOUT TIME ZONE
);
```

## Columns

- id
    - Surrogate primary key.
- task
    - Name of the scheduled task being run.
- created
    - When this row was initially created.
