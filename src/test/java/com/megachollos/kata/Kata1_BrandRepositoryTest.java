package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import com.megachollos.model.domain.ModelRepository;
import com.megachollos.model.jpa.repositories.JpaModelRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class Kata1_BrandRepositoryTest {

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private JpaModelRepository jpaModelRepository;

  @BeforeEach
  void setUp() {
    jpaModelRepository.deleteAllInBatch();
    jpaBrandRepository.deleteAllInBatch();
  }

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
    jpaBrandRepository.saveAndFlush(brand);

    // THEN we can find it
    var result = jpaBrandRepository.findById("samsung");
    assertThat(result).isPresent();
    assertThat(result.get().getDisplayName()).isEqualTo("Samsung");
  }

  @Test
  void shouldFailWhenDuplicateUniqueName(){

    jpaBrandRepository.deleteById("samsung");
    jpaBrandRepository.flush();

    BrandEntity brand = BrandEntity.builder()
            .uniqueName("samsung")
            .displayName("Samsung")
            .build();
    jpaBrandRepository.saveAndFlush(brand); // Guardamos y forzamos escritura

    entityManager.clear();

    BrandEntity repeatedBrand = BrandEntity.builder()
            .uniqueName("samsung")
            .displayName("different Samsung")
            .build();

    // WHEN we save it
/*    assertThatThrownBy(() -> {
      entityManager.persist(repeatedBrand);
      entityManager.flush();
    }).isInstanceOf(org.springframework.dao.DataIntegrityViolationException.class); */

    assertThatThrownBy(() -> {
      entityManager.persist(repeatedBrand);
      entityManager.flush();
    }).hasMessageContaining("duplicate key");
  }
}
