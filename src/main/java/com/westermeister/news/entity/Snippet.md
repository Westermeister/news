# Snippet

## Schema

```sql
CREATE TABLE snippet (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    slot SMALLINT,
    summary CHARACTER VARYING(255),
    source CHARACTER VARYING(255),
    last_updated TIMESTAMP(0) WITHOUT TIME ZONE
);
```

## Columns

- id
    - Surrogate primary key.
- slot
    - Should be an integer from 0 to 9.
    - The app only displays the top 10 news snippets, in order that they're retrieved from the NYTimes API.
    - The order corresponds to the slot number.
    - When the news is refreshed, the existing slots are replaced with new ones.
- summary
    - The news snippet that's displayed.
- source
    - Link (URL) to the source.
- last_updated
    - Timestamp when this slot was last updated.
