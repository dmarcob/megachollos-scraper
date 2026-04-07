# Kata 1 — Why Integration Tests?

## Goal

Understand why mocking the database isn't enough. Fix a broken test so it runs against a real PostgreSQL.

## Context

Open the file `src/test/java/com/megachollos/kata/Kata1_BrandRepositoryTest.java`. This test tries to save a Brand to the database and read it back. But it's broken.

## Tasks

### Step 1 — Run the broken test

Run `Kata1_BrandRepositoryTest` from your IDE or with:
```bash
mvn test -Dtest=Kata1_BrandRepositoryTest
```

Read the error carefully. What is it telling you?

### Step 2 — Understand the problem

The test uses `@DataJpaTest`, which by default tries to use an H2 in-memory database. But this project doesn't have H2 — it uses PostgreSQL. We need a real PostgreSQL for our tests.

### Step 3 — Add the Testcontainers PostgreSQL dependency

Add this dependency to `pom.xml` inside `<dependencies>`:
```xml
<dependency>
  <groupId>org.testcontainers</groupId>
  <artifactId>postgresql</artifactId>
  <version>1.21.4</version>
  <scope>test</scope>
</dependency>
```

### Step 4 — Fix the test

Modify `Kata1_BrandRepositoryTest.java`:

1. Add these annotations to the class:
   - `@Testcontainers`
   - `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)`

2. Add a PostgreSQL container field:
   ```java
   @Container
   @ServiceConnection
   static PostgreSQLContainer<?> postgres =
       new PostgreSQLContainer<>("postgres:17-alpine");
   ```

3. Add the necessary imports (your IDE should suggest them)

### Step 5 — Run the test again

```bash
mvn test -Dtest=Kata1_BrandRepositoryTest
```

It should pass now. You just tested against a real PostgreSQL running in a Docker container!

### Step 6 — Bonus

Add a new test method `shouldFailWhenDuplicateUniqueName()` that:
1. Saves a BrandEntity with uniqueName "samsung"
2. Tries to save another BrandEntity with the same uniqueName "samsung"
3. Asserts that a `DataIntegrityViolationException` is thrown

> This is something a mock would never catch — but a real database enforces the PRIMARY KEY constraint.
