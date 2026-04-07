Block 4 — Testcontainers

Two options for integration testing with a database
Option 1 — H2 embeddedOption 2 — TestcontainersFast startupvsSlower startupNo Docker neededRequires DockerDifferent SQL dialectReal PostgreSQL dialectPartial constraint enforcementFull constraint enforcementNot what runs in productionIdentical to production

How Testcontainers fits in your stack
Your test code          Testcontainers           Docker engine
@Testcontainers   calls→  Java library      calls→  docker run
@Container                manages lifecycle          docker stop
@ServiceConnection        wires connection           docker rm

@Testcontainers, @Container and @ServiceConnection
java@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Testcontainers // 1 — activates lifecycle management on this class
class UserRepositoryTest {

    @Container // 2 — marks this field as a managed container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void shouldSaveUser() {
        // postgres is already running here
        // @Testcontainers + @Container started it before this test ran
    }
}

How @Testcontainers and @Container work together
JUnit starts    →   @Testcontainers   →   docker run          →   PostgreSQL
test class          finds @Container       postgres:16-alpine      running on :RANDOM

                              ↓
                         tests run
                  PostgreSQL available, queries execute for real

                              ↓
                         docker stop
                          docker rm

✅ Static container + @BeforeEach cleanup
java@SpringBootTest
@Testcontainers
class UserServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres =
        new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldSaveUser() {
        // GIVEN a clean database
        // WHEN
        userRepository.save(new User("Pep", "pep@gmail.com"));
        // THEN
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldSaveAnotherUser() {
        // GIVEN a clean database — deleteAll ran again
        // WHEN
        userRepository.save(new User("Ana", "ana@gmail.com"));
        // THEN
        assertThat(userRepository.count()).isEqualTo(1);
    }
}

❌ Non static container
java@SpringBootTest
@Testcontainers
class UserServiceTest {

    @Container
    @ServiceConnection
    PostgreSQLContainer<?> postgres = // no static
        new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UserRepository userRepository;

    // no cleanup needed — fresh container per test

    @Test
    void shouldSaveUser() {
        // GIVEN a brand new empty database
        // WHEN
        userRepository.save(new User("Pep", "pep@gmail.com"));
        // THEN
        assertThat(userRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldSaveAnotherUser() {
        // GIVEN a brand new empty database
        // WHEN
        userRepository.save(new User("Ana", "ana@gmail.com"));
        // THEN
        assertThat(userRepository.count()).isEqualTo(1);
    }
}