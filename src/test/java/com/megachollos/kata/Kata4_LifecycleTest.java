package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.brand.infrastructure.jpa.entities.BrandEntity;
import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

@EnablePostgresTest
class Kata4_LifecycleTest {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

  private static long startTimestamp;

  @Autowired
  private JpaBrandRepository jpaBrandRepository;

  @BeforeAll
  static void startTimer() {
    startTimestamp = System.currentTimeMillis();
    System.out.println("START: " + startTimestamp);
  }

  @AfterAll
  static void endTimer() {
    long endTimestamp = System.currentTimeMillis();
    System.out.println("END: " + endTimestamp);
    System.out.println("DURATION_MS: " + (endTimestamp - startTimestamp));
  }

  @BeforeEach
  void clearDatabaseBeforeEachTest() {
    jpaBrandRepository.deleteAll();
  }

  @Test
  void shouldPersistSingleBrandForSamsungScenario() {
    jpaBrandRepository.save(BrandEntity.builder()
        .uniqueName("samsung")
        .displayName("Samsung")
        .build());

    System.out.println("Samsung scenario - Port: " + postgres.getMappedPort(5432));
    assertThat(jpaBrandRepository.count()).isEqualTo(1);
  }

  @Test
  void shouldPersistSingleBrandForAppleScenario() {
    jpaBrandRepository.save(BrandEntity.builder()
        .uniqueName("apple")
        .displayName("Apple")
        .build());

    System.out.println("Apple scenario - Port: " + postgres.getMappedPort(5432));
    assertThat(jpaBrandRepository.count()).isEqualTo(1);
  }
}
