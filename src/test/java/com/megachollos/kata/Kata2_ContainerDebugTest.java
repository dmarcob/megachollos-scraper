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
  void shouldStartContainerWithValidImage() {
    assertThat(postgresA.isRunning()).isTrue();
  }

  @Container
  static PostgreSQLContainer<?> postgresB =
      new PostgreSQLContainer<>("postgres:17-alpine")
          .withUsername("megachollos")
          .withPassword("megachollos")
          .withDatabaseName("megachollos");

  @Test
  void shouldConnectUsingConfiguredCredentials() {
    try (var connection = java.sql.DriverManager.getConnection(
        postgresB.getJdbcUrl(), "megachollos", "megachollos")) {
      var selectOneResult = connection.createStatement().executeQuery("SELECT 1");
      assertThat(selectOneResult.next()).isTrue();
    } catch (Exception exception) {
      throw new RuntimeException("Cannot connect to database with configured credentials", exception);
    }
  }

  @Container
  static PostgreSQLContainer<?> postgresC =
      new PostgreSQLContainer<>("postgres:17-alpine")
          .withUsername("test")
          .withPassword("test")
          .withDatabaseName("test");

  @Test
  void shouldExposeContainerConnectionDetails() {
    System.out.println("=== Container is running ===");
    System.out.println("JDBC URL: " + postgresC.getJdbcUrl());
    System.out.println("Host: " + postgresC.getHost());
    System.out.println("Port: " + postgresC.getMappedPort(5432));
    System.out.println("Open another terminal and run: docker ps");
    System.out.println("Then connect with: docker exec -it <CONTAINER_ID> psql -U test -d test");

    // Uncomment temporarily if you want enough time to inspect the container manually.
    // Thread.sleep(60000);

    assertThat(postgresC.isRunning()).isTrue();
  }
}
