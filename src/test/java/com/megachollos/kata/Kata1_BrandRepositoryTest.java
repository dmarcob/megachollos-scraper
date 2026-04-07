package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

/**
 * Kata 1 — Fixed: now runs against a real PostgreSQL using Testcontainers.
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

  @Autowired
  private TestEntityManager entityManager;

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

  @Test
  void shouldFailWhenDuplicateUniqueName() {
    BrandEntity brand1 = BrandEntity.builder()
        .uniqueName("samsung")
        .displayName("Samsung")
        .build();
    entityManager.persistAndFlush(brand1);
    entityManager.clear();

    BrandEntity brand2 = BrandEntity.builder()
        .uniqueName("samsung")
        .displayName("Samsung ")
        .build();

    assertThatThrownBy(() -> entityManager.persistAndFlush(brand2))
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}
