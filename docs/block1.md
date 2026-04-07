Block 1 — Integration testing fundamentals

Unit test vs integration test vs end-to-end test
Testing pyramid (top to bottom):

E2E — Few, slow, expensive. Test full user flows.
Integration — Test components together. Real DB, real services.
Unit — Many, fast, cheap. Test single classes.

(Axes: many → few, fast → slow)

Why integration tests matter for persistence
UserService.java
java@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User register(String name, String email) {
        User user = new User(name, email);
        return userRepository.save(user);
    }
}
User.java (Entity)
java@Entity
public class User {
@Id
@GeneratedValue
private Long id;

    @Column(unique = true)
    private String email;

    private String name;
}
Questions on slide: "What is this code doing?" / "What happens when the Spring Boot app runs?"

Unit test (Mockito) — UserServiceTest.java
java@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldRegisterUser() {
        // GIVEN a valid user
        User savedUser = new User("Pep", "pep@gmail.com");
        when(userRepository.save(any())).thenReturn(savedUser);

        // WHEN we register the user
        User result = userService.register("Pep", "pep@gmail.com");

        // THEN the user is returned
        assertThat(result.getEmail()).isEqualTo("pep@gmail.com");
    }
}
Integration test (@DataJpaTest) — UserRepositoryTest.java
java@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFailWhenEmailIsDuplicated() {
        // GIVEN a user already exists with email pep@gmail.com
        userRepository.save(new User("Pep", "pep@gmail.com"));

        // WHEN we try to save another user with the same email
        // THEN a DataIntegrityViolationException is thrown
        assertThatThrownBy(() ->
            userRepository.save(new User("Other", "pep@gmail.com"))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}

The problem with mocking the database

When you mock the repository you are not testing your code against a database. You are testing your code against your assumptions about the database

java@Column(nullable = false)  // field cannot be null
@Column(unique = true)     // value must be unique across all rows
@Column(length = 50)       // varchar max length — PostgreSQL enforces this