package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * This test is BROKEN on purpose.
 * Your mission: make it work with a real PostgreSQL using Testcontainers.
 * Follow the instructions in kata-1-why-integration-tests.md
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class Kata1_BrandRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
          new PostgreSQLContainer<>("postgres:17-alpine");

  @Autowired
  private JpaBrandRepository jpaBrandRepository;



  @Test
  void shouldSaveAndFindBrand() {
    // GIVEN a brand
    BrandEntity brand = BrandEntity.builder()
        .uniqueName("samsung")
        .displayName("Samsung")
        .build();

    // WHEN we save it
    jpaBrandRepository.save(brand);

    // THEN we can find it
    var result = jpaBrandRepository.findById("samsung");
    assertThat(result).isPresent();
    assertThat(result.get().getDisplayName()).isEqualTo("Samsung");
  }

  /*@Test
    void shouldReturnEmptyWhenBrandDoesNotExist() {
        // WHEN
        var result = jpaBrandRepository.findById("non-existent");

        // THEN
        assertThat(result).isEmpty();
    }

    @BeforeEach
    void setUp() {
        // Limpieza explícita
        jpaBrandRepository.deleteAll();
        jpaBrandRepository.flush(); // Forzamos a que la base de datos se actualice
    }

    */
}
