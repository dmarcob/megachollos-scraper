package com.megachollos.kata;

import static com.megachollos.kata.Kata1_BrandRepositoryTest.postgres;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * This test has 3 exercises with broken container configurations.
 * Fix them one by one following kata-2-containers.md
 */
@Testcontainers
class Kata2_ContainerDebugTest {

  // =============================================
  // Exercise A — Wrong image tag
  // TODO fix me: this image tag does not exist
  // =============================================
  @Container
  static PostgreSQLContainer<?> postgresA =
      new PostgreSQLContainer<>("postgres:17-alpine");

  @Test
  void exerciseA_wrongImage() {
    assertThat(postgresA.isRunning()).isTrue();
  }

  // =============================================
  // Exercise B — Wrong credentials
  // TODO fix me: the container credentials don't match what the test expects
  // =============================================
  @Container
  static PostgreSQLContainer<?> postgresB =
      new PostgreSQLContainer<>("postgres:17-alpine")
          .withUsername("megachollos")
          .withPassword("megachollos")
          .withDatabaseName("megachollos");

  @Test
  void exerciseB_wrongCredentials() {
    // This test connects with username "megachollos", password "megachollos", database "megachollos"
    // Fix the container above so these credentials work
    try (var conn = java.sql.DriverManager.getConnection(
        postgresB.getJdbcUrl(), "megachollos", "megachollos")) {
      var result = conn.createStatement().executeQuery("SELECT 1");
      assertThat(result.next()).isTrue();
    } catch (Exception e) {
      throw new RuntimeException("Cannot connect to database — fix the container credentials!", e);
    }
  }

  // =============================================
  // Exercise C — Observe the container
  // Run this test, then open another terminal and run: docker ps
  // =============================================
  @Container
  static PostgreSQLContainer<?> postgresC =
      new PostgreSQLContainer<>("postgres:17-alpine")
          .withUsername("test")
          .withPassword("test")
          .withDatabaseName("test");

  @Test
  void exerciseC_observeContainer() throws InterruptedException {
    System.out.println("=== Container is running! ===");
    System.out.println("JDBC URL: " + postgresC.getJdbcUrl());
    System.out.println("Host: " + postgresC.getHost());
    System.out.println("Port: " + postgresC.getMappedPort(5432));
    System.out.println();
    System.out.println("Open another terminal and run: docker ps");
    System.out.println("Then connect with: docker exec -it <CONTAINER_ID> psql -U test -d test");

    System.out.println(postgres.getLogs());
    // TODO: Uncomment the line below, run the test, observe with docker ps,
    //       then comment it back when done
    // Thread.sleep(300000);

    assertThat(postgresC.isRunning()).isTrue();
  }
}
