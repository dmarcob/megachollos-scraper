Block 3 — Spring Boot persistence testing layer

@SpringBootTest — what it does, when to use it
@SpringBootTest@DataJpaTestFull application contextvsJPA slice onlyControllers loadedControllers — NOT loadedServices loadedServices — NOT loadedRepositories loadedRepositories loadedSecurity loadedSecurity — NOT loadedEverything else loadedEverything else — NOT loadedSlow to startFast to startUse for full flow testsUse for repository tests

@DataJpaTest — what it does, difference from @SpringBootTest
java@DataJpaTest // rollback is automatic
class UserRepositoryTest {

    @Test
    void test1() {
        userRepository.save(new User("Pep", "pep@gmail.com"));
        // rolled back after this test
    }

    @Test
    void test2() {
        // database is clean here — test1 data is gone
    }
}

@Sql — seeding data from SQL files
java@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers
class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql("/seed-users.sql")
    void shouldFindUserByEmail() {
        // GIVEN 3 users seeded by seed-users.sql

        // WHEN we search by email
        Optional<User> result = userRepository
            .findByEmail("pep@gmail.com");

        // THEN we find the user
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Pep");
    }
}
seed-users.sql (src/test/resources/seed-users.sql)
sqlINSERT INTO users (id, name, email) VALUES (1, 'Pep', 'pep@gmail.com');
INSERT INTO users (id, name, email) VALUES (2, 'Ana', 'ana@gmail.com');
INSERT INTO users (id, name, email) VALUES (3, 'Juan', 'juan@gmail.com');