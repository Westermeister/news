# Overview

This document details how to test the project.

## Context

Currently, we have test suites that cover the most important parts of the project:

1. Extracting and pushing data from / to APIs.
2. Stateful interactions involving user accounts e.g. signing up, updating account, signing in, deleting account, etc.

The first test suite is a typical JUnit test suite with mocks, etc.

The second is a fully end-to-end test that uses [Playwright](https://playwright.dev/java/).

## How to run the tests

In order to run the tests, do the following:

1. Comment out `@EnableScheduling` in `NewsApplication.java`:

```java
@SpringBootApplication
// @EnableScheduling
public class NewsApplication {
    ...
}
```

2. Prepare a clean schema in PostgreSQL:

```sql
drop schema public cascade;
create schema public;
```

3. Clean any existing build files:

```bash
./gradlew clean
```

4. Start the server:

```bash
./gradlew bootRun
```

5. Start the tests:

```bash
./gradlew test
```

6. Repeat steps 2 - 5 for subsequent tests.

For convenience, you can have multiple separate terminal instances so that you can quickly do each of the steps 2 - 5.

This is a bit of a crude procedure, and could probably use some improvements, but it's sufficient for now.
