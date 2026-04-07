package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * This test is BROKEN on purpose.
 * Your mission: make it work with a real PostgreSQL using Testcontainers.
 * Follow the instructions in kata-1-why-integration-tests.md
 */
@DataJpaTest
class Kata1_BrandRepositoryTest {

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
}
