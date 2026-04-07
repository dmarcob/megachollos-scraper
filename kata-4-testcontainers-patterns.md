# Kata 4 — Testcontainers Patterns

## Goal

Understand container lifecycle: static vs non-static containers, cleanup strategies, and startup cost.

## Tasks

Create a new file: `src/test/java/com/megachollos/kata/Kata4_LifecycleTest.java`

### Exercise A — Non-static container (one container per test)

1. Create a test class with:
   ```java
   @DataJpaTest
   @Testcontainers
   @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
   @TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
   ```

2. Add a **non-static** container (no `static` keyword):
   ```java
   @Container
   @ServiceConnection
   PostgreSQLContainer<?> postgres =
       new PostgreSQLContainer<>("postgres:17-alpine");
   ```

3. Inject `JpaBrandRepository`

4. Write two tests:
   ```java
   @Test
   void test1() {
       jpaBrandRepository.save(BrandEntity.builder()
           .uniqueName("samsung").displayName("Samsung").build());
       System.out.println("Test 1 - Port: " + postgres.getMappedPort(5432));
       assertThat(jpaBrandRepository.count()).isEqualTo(1);
   }

   @Test
   void test2() {
       jpaBrandRepository.save(BrandEntity.builder()
           .uniqueName("apple").displayName("Apple").build());
       System.out.println("Test 2 - Port: " + postgres.getMappedPort(5432));
       assertThat(jpaBrandRepository.count()).isEqualTo(1);
   }
   ```

5. Run both tests. Observe:
   - Both pass (each gets a fresh database)
   - The ports are **different** (two separate containers)

### Exercise B — Static container (shared across tests)

1. Change the container to `static`:
   ```java
   @Container
   @ServiceConnection
   static PostgreSQLContainer<?> postgres = ...
   ```

2. Run both tests. What happens?
   - **test2 fails** — count is 2 because test1's data is still there

3. Fix it by adding cleanup:
   ```java
   @Autowired
   private JpaBrandRepository jpaBrandRepository;

   @BeforeEach
   void cleanup() {
       jpaBrandRepository.deleteAll();
   }
   ```

4. Run again — both pass. The ports are now the **same** (one shared container).

### Exercise C — Measure the difference

1. Add timing to your tests:
   ```java
   @BeforeAll
   static void startTimer() {
       System.out.println("START: " + System.currentTimeMillis());
   }

   @AfterAll
   static void endTimer() {
       System.out.println("END: " + System.currentTimeMillis());
   }
   ```

2. Run with **non-static** container — note the total time
3. Run with **static** container — note the total time
4. Write the difference as a comment in your code:
   ```java
   // Non-static: ~Xs (N container startups)
   // Static: ~Ys (1 container startup)
   ```

### Bonus — Custom meta-annotation

Create a custom annotation that combines all the boilerplate:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
public @interface EnablePostgresTest {
}
```

Write a new test `Kata4_MetaAnnotationTest.java` that uses `@EnablePostgresTest` instead of the three separate annotations. Verify it works.
