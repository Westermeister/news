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

- task
    - Name of the scheduled task being run.
    - Acts as a lock to prevent multiple nodes duplicating the same task.
