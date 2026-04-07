package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@EnablePostgresTest
class Kata4_MetaAnnotationTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

  @Autowired
  private JpaBrandRepository brandRepository;

  @Test
  void shouldWorkWithMetaAnnotation() {
    brandRepository.save(BrandEntity.builder()
        .uniqueName("sony")
        .displayName("Sony")
        .build());

    assertThat(brandRepository.findById("sony")).isPresent();
  }
}
