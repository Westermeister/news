# Overview

- This file documents the entire database schema of this project.
- It includes every table and column, as well as a list of conventions that they follow.
- You should think of this document as the **single source of truth** for all things database-related.
- Everything database-related must submit to this document's rules.

## Table of Contents

- [Conventions](#conventions)
    - [Naming](#naming)
    - [Primary Keys](#primary-keys)
    - [Foreign Keys](#foreign-keys)
    - [Junction Tables](#junction-tables)
    - [Entity Classes](#entity-classes)
- [Tables](#tables)
    - [User](#user)
    - [Session](#session)

# Conventions

## Naming

- All database identifiers will use lowercase letters and underscores.
- If an identifier is a reserved word, a trailing underscore will be appended to it.
    - e.g. `user --> user_`
    - The additional underscore will be ignored when naming identifiers involving the table (more details later on).

## Primary Keys

All tables (excepting junction tables) will contain a surrogate primary key as follows:

```sql
CREATE TABLE ... (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    ...
);
```

Rationale:

- The `BIGINT` is future-proof without being too large like `UUID` is.
- The `GENERATED ALWAYS AS IDENTITY` makes the key read-only.
    - Primary keys should (ideally) never change.

## Foreign Keys

All foreign keys will have the following format:

```sql
--- Here, `col` refers to the foreign key column name inside `table1`.
CONSTRAINT fk__table1__col__table2 FOREIGN KEY (col) REFERENCES table2 (id)
```

Optionally, you can add:

```sql
--- Or `ON UPDATE`!
ON DELETE CASCADE
```

One thing to note is that some tables might have trailing underscores to avoid reserved words in SQL.
That won't be reflected when defining the foreign key constraint.

## Junction Tables

Junction tables will all use the following syntax:

```sql
CREATE TABLE a_b (
    a_id BIGINT,
    b_id BIGINT,
    PRIMARY KEY (a_id, b_id),
    CONSTRAINT fk__a_b__a_id__a FOREIGN KEY (a_id) REFERENCES a (id),
    CONSTRAINT fk__a_b__b_id__b FOREIGN KEY (b_id) REFERENCES b (id)
);

CREATE INDEX ON a_b (b_id, a_id);
```

- The table name convention is alphabetical order, separated by an underscore.
- Like with foreign keys, trailing underscores in table names will be ignored.

## Entity Classes

- For some context, this project uses Spring Data JPA on top of Hibernate, so it involves writing entity classes.
- **For every table, there is a corresponding entity class.**
- Some of the nitty-gritty details in this document are only relevant to migration scripts and not entity classes.
- At the same time, there are also some additional (non-SQL-related) rules that apply to them.
- Let's lay them out:
    1. Entity classes must specify the correct table name, column names, and column data types.
    2. If they include associations, they can ONLY be unidirectional `@ManyToOne` relationships.
    3. ...and those relationships MUST be fetched lazily.
    4. They should never specify any constraints like `length`, `nullable`, `unique`, etc.
        - These are all handled at the database level.
    5. They must include a no-args, protected constructor.
    6. They must also include a public constructor that lets clients initialize them with values for all non-primary-key
       fields.
    7. They must have a `toString` method that converts the entity instance into a human-readable form with all columns.
    8. They must have getters and setters for every field, with the exception of the primary key field, which should
       only have a getter.
    9. In addition to all of the above, they should comply with all conventions listed in this document.
        - For instance, one of the conventions is that every non-junction table needs a 64-bit surrogate key.
        - Thus, every non-junction entity class should include a `Long id` field.

That's a lot to take in, so here's a good example:

```java
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    protected Car() {}

    public Car(Owner owner, String make, String model) {
        this.owner = owner;
        this.make = make;
        this.model = model;
    }

    @Override
    public String toString() {
        return String.format(
            "Session [id=%d, owner=%d, make=%s, model=%s]",
            id, owner.getId(), make, model
        );
    }

    public Long getId() {
        return id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
```

# Tables

- The rest of this document lists all the tables and columns in the database schema.
- Any time a table is created, updated, or deleted, that change must first be reflected here in this document.
- Any other work can only proceed AFTER first documenting the table here.

## User

```sql
CREATE TABLE user_ (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255),
    created BIGINT,
    last_sign_in BIGINT
);
```
