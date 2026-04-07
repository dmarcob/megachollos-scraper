package com.megachollos.testUtils.integration.postgres;

import com.megachollos.testUtils.integration.BaseDockerComposeExtension;
import com.megachollos.testUtils.integration.DockerComposeInfo;
import com.megachollos.testUtils.integration.TestProperties;
import java.io.File;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

@Slf4j
public class PostgresTestExtension extends BaseDockerComposeExtension {

  private static DockerComposeContainer<?> postgresContainer;

  PostgresTestExtension() {
    super.dockerComposeInfo = DockerComposeInfo.builder()
        .serviceName("postgres-db")
        .servicePort(5432)
        .ymlFile("docker-compose-postgres.yml").build();
  }

  @Override
  protected DockerComposeContainer<?> getDockerContainer() {
    return postgresContainer;
  }

  @Override
  protected void instantiate(File file) {
    postgresContainer = new DockerComposeContainer<>(file)
        .withExposedService(dockerComposeInfo.getServiceName(), dockerComposeInfo.getServicePort())
        .waitingFor(dockerComposeInfo.getServiceName(),
            Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));
  }

  @Override
  protected void beforeAllTests(ExtensionContext junitContext) {
    TestProperties.setPostgresProperties(getJdbcUrl());

    EnablePostgresTests annotation = AnnotationUtils.findAnnotation(
        junitContext.getRequiredTestClass(), EnablePostgresTests.class);

    if (annotation != null && StringUtils.isNotBlank(annotation.sqlScriptBeforeAll())) {
      executeSqlScript(junitContext, annotation.sqlScriptBeforeAll());
    }
  }

  @Override
  protected void beforeEachTest(ExtensionContext junitContext) {
    // Do nothing
  }

  @Override
  protected void afterEachTest(ExtensionContext junitContext) {
    deleteAllDatabaseRecords(junitContext);
    resetSequences(junitContext);
  }

  @Override
  protected void afterAllTests(ExtensionContext junitContext) {
    // Do nothing
  }

  private void executeSqlScript(ExtensionContext extensionContext, String scriptName) {
    DataSource dataSource = SpringExtension.getApplicationContext(extensionContext)
        .getBean(DataSource.class);
    Resource resource = new ClassPathResource(scriptName);
    ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);
    databasePopulator.execute(dataSource);
  }

  private void deleteAllDatabaseRecords(ExtensionContext extensionContext) {
    DataSource dataSource = SpringExtension.getApplicationContext(extensionContext)
        .getBean(DataSource.class);
    try (var connection = dataSource.getConnection();
         var statement = connection.createStatement()) {
      var rs = statement.executeQuery(
          "SELECT tablename FROM pg_tables WHERE schemaname = 'public'");
      while (rs.next()) {
        String tableName = rs.getString("tablename");
        if (!tableName.startsWith("megachollos_") && !tableName.startsWith("databasechangelog")) {
          statement.execute("TRUNCATE TABLE \"" + tableName + "\" CASCADE");
        }
      }
    } catch (Exception e) {
      log.debug("Error truncating tables: {}", e.getMessage());
    }
  }

  protected String getJdbcUrl() {
    String serviceName = dockerComposeInfo.getServiceName();
    Integer servicePort = dockerComposeInfo.getServicePort();
    Assert.assertNotNull(serviceName);
    Assert.assertNotNull(servicePort);

    ContainerState containerState = getContainerState();
    Assert.assertTrue(containerState.isRunning());

    return "jdbc:postgresql://" + postgresContainer.getServiceHost(serviceName, servicePort)
        + ":" + postgresContainer.getServicePort(serviceName, servicePort) + "/megachollos";
  }

  private void resetSequences(ExtensionContext context) {
    DataSource dataSource = SpringExtension.getApplicationContext(context)
        .getBean(DataSource.class);
    try (var connection = dataSource.getConnection();
         var statement = connection.createStatement()) {
      var rs = statement.executeQuery(
          "SELECT sequencename FROM pg_sequences WHERE schemaname = 'public'");
      while (rs.next()) {
        String seqName = rs.getString("sequencename");
        statement.execute("ALTER SEQUENCE \"" + seqName + "\" RESTART WITH 1");
      }
    } catch (Exception e) {
      log.warn("Error resetting sequences: {}", e.getMessage());
    }
  }
}
