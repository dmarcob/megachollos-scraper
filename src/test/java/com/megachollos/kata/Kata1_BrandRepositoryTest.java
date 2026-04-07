package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;


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
  private EntityManager entityManager;

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
    jpaBrandRepository.saveAndFlush(brand1);
    entityManager.detach(brand1);

    BrandEntity brand2 = BrandEntity.builder()
        .uniqueName("samsung")
        .displayName("Samsung Electronics")
        .build();

    assertThatThrownBy(() -> jpaBrandRepository.saveAndFlush(brand2))
        .isInstanceOf(DataIntegrityViolationException.class);
  }
}
