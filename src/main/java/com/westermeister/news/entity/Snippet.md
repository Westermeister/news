# Snippet

## Schema

```sql
CREATE TABLE snippet (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    slot SMALLINT UNIQUE,
    summary CHARACTER VARYING(1000),
    source CHARACTER VARYING(1000),
    last_updated TIMESTAMP(0) WITHOUT TIME ZONE
);
```

## Columns

- id
    - Surrogate primary key.
- slot
    - Should be an integer, >= 0.
    - The "slot" represents a spot on the homepage's list for displaying a news snippet.
    - So for example, slot 0 is the first news snippet, slot 1 is the next snippet, and so on.
- summary
    - The text of the news snippet that's displayed.
- source
    - Link (URL) to the source.
- last_updated
    - Timestamp when this slot was last updated.
