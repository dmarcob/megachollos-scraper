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
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class Kata1_BrandRepositoryTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

  @Autowired
  private JpaBrandRepository jpaBrandRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void shouldSaveAndFindBrand() {
    BrandEntity brandToPersist = BrandEntity.builder()
        .uniqueName("samsung")
        .displayName("Samsung")
        .build();

    jpaBrandRepository.save(brandToPersist);

    var savedBrand = jpaBrandRepository.findById("samsung");
    assertThat(savedBrand).isPresent();
    assertThat(savedBrand.get().getDisplayName()).isEqualTo("Samsung");
  }

  @Test
  void shouldFailWhenDuplicateUniqueName() {
    BrandEntity firstBrand = BrandEntity.builder()
        .uniqueName("samsung")
        .displayName("Samsung")
        .build();

    BrandEntity duplicatedBrand = BrandEntity.builder()
        .uniqueName("samsung")
        .displayName("Samsung Duplicate")
        .build();

    jpaBrandRepository.saveAndFlush(firstBrand);

    assertThatThrownBy(() -> jdbcTemplate.update(
        "INSERT INTO brands (unique_name, display_name) VALUES (?, ?)",
        duplicatedBrand.getUniqueName(),
        duplicatedBrand.getDisplayName()
    )).isInstanceOf(DataIntegrityViolationException.class);
  }
}
