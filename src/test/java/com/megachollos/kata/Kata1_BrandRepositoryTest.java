package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import com.megachollos.model.jpa.repositories.JpaModelRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * This test is BROKEN on purpose.
 * Your mission: make it work with a real PostgreSQL using Testcontainers.
 * Follow the instructions in kata-1-why-integration-tests.md
 */
@DataJpaTest
//@ActiveProfiles("test")
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE )
class Kata1_BrandRepositoryTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17-alpine");

  @Autowired
  private JpaBrandRepository jpaBrandRepository;

  @Autowired
  private EntityManager entityManager;
  @Autowired
  private JpaModelRepository jpaModelRepository;
  @BeforeEach
  void setUp() {
    jpaModelRepository.deleteAllInBatch();
    jpaBrandRepository.deleteAllInBatch();
  }

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
  void shouldFailWhenDuplicateUniqueName(){
    // GIVEN a saved brand
    BrandEntity brand1 = BrandEntity.builder()
            .uniqueName("samsung2")
            .displayName("Samsung")
            .build();
    jpaBrandRepository.saveAndFlush(brand1);
    entityManager.clear();

    // AND a new brand of the same name
    BrandEntity brand2 = BrandEntity.builder()
            .uniqueName("samsung2")
            .displayName("Samsung")
            .build();

    // WHEN we save the second brand
    // THEN we catch the error
    //assertThrows(DataIntegrityViolationException.class,
    //        () -> jpaBrandRepository.saveAndFlush(brand2));

    assertThatThrownBy(() -> {
      entityManager.persist(brand2);
      entityManager.flush();
    }).hasMessageContaining("duplicate key");

  }
}
