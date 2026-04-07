package com.megachollos.kata;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
class Kata2_ContainerDebugTest {

  @Container
  static PostgreSQLContainer<?> postgresA =
      new PostgreSQLContainer<>("postgres:17-alpine");

  @Test
  void exerciseA_wrongImage() {
    assertThat(postgresA.isRunning()).isTrue();
  }


  @Container
  static PostgreSQLContainer<?> postgresB =
      new PostgreSQLContainer<>("postgres:17-alpine")
          .withUsername("megachollos")
          .withPassword("megachollos")
          .withDatabaseName("megachollos");

  @Test
  void exerciseB_wrongCredentials() {
    // This test connects with username "megachollos", password "megachollos", database "megachollos"
    try (var conn = java.sql.DriverManager.getConnection(
        postgresB.getJdbcUrl(), "megachollos", "megachollos")) {
      var result = conn.createStatement().executeQuery("SELECT 1");
      assertThat(result.next()).isTrue();
    } catch (Exception e) {
      throw new RuntimeException("Cannot connect to database — fix the container credentials!", e);
    }
  }


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

    System.out.println("\n logs ");
    System.out.println(postgresC.getLogs());

    assertThat(postgresC.isRunning()).isTrue();
  }
}
