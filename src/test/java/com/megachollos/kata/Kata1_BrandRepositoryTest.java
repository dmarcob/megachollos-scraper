package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.DataIntegrityViolationException;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class Kata1_BrandRepositoryTest {

  @Autowired
  private JpaBrandRepository jpaBrandRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgres =
          new PostgreSQLContainer<>("postgres:17-alpine");

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
    // GIVEN a brand already saved
    BrandEntity brand = BrandEntity.builder()
            .uniqueName("samsung")
            .displayName("Samsung")
            .build();
    jpaBrandRepository.save(brand);
    jpaBrandRepository.flush();

    // WHEN we try to insert another with the same uniqueName
    BrandEntity duplicate = BrandEntity.builder()
            .uniqueName("samsung")
            .displayName("Samsung Duplicado")
            .build();

    // THEN it should throw DataIntegrityViolationException
    assertThatThrownBy(() -> {
      entityManager.clear();
      entityManager.persist(duplicate);
      entityManager.flush();
    }).isInstanceOfAny(
            DataIntegrityViolationException.class,
            org.hibernate.exception.ConstraintViolationException.class);
  }
}