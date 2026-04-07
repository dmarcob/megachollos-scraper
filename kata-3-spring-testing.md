# Kata 3 — Spring Boot Testing with Testcontainers

## Goal

Write persistence tests from scratch using `@DataJpaTest`, `@Sql`, and Testcontainers.

## Tasks

Create a new file: `src/test/java/com/megachollos/kata/Kata3_CategoryRepositoryTest.java`

### Step 1 — Set up the test class

Create the class with these annotations:
- `@DataJpaTest`
- `@Testcontainers`
- `@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)`

Add a static PostgreSQL container

Inject a `JpaCategoryRepository` repository:

### Step 2 — Write `shouldSaveAndFindCategory()`

1. Create a `CategoryEntity` with `uniqueName = "electronica"` and `displayName = "Electronica"`
2. Save it with `categoryRepository.save(...)`
3. Find it with `categoryRepository.findById("electronica")`
4. Assert:
   - The result is present
   - `uniqueName` equals "electronica"
   - `displayName` equals "Electronica"

### Step 3 — Write `shouldSaveCategoryWithParent()`

1. Create and save a parent category: `uniqueName = "tech"`, `displayName = "Technology"`
2. Create a child category: `uniqueName = "laptops"`, `displayName = "Laptops"`, `parentCategoryUniqueName = "tech"`
3. Save the child category
4. Find the child by ID and assert:
   - The parent category is not null
   - The parent's uniqueName is "tech"

### Step 4 — Seed data with `@Sql`

1. Create a file `src/test/resources/seed-categories.sql`:
   ```sql
   INSERT INTO categories (unique_name, display_name, parent_category) VALUES ('root1', 'Root One', NULL);
   INSERT INTO categories (unique_name, display_name, parent_category) VALUES ('root2', 'Root Two', NULL);
   INSERT INTO categories (unique_name, display_name, parent_category) VALUES ('child1', 'Child One', 'root1');
   ```

2. Write a test `shouldFindSeededCategories()` annotated with `@Sql("/seed-categories.sql")`:
   - Assert `categoryRepository.count()` equals 3
   - Assert `findById("child1")` returns a category with parent "root1"

### Step 5 — Bonus

Write `shouldFindRootCategories()`:
- Use the `@Sql` annotation to seed the same data
- Query `categoryRepository.findAll()` and filter categories where parent is null
- Assert there are exactly 2 root categories

## Hints

- Look at `CategoryEntity.java` to understand the fields and relationships
- `JpaCategoryRepository` is a standard Spring Data JPA interface — check what methods are available
- `@Sql` runs the SQL file before each test method by default
