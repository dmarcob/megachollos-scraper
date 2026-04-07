package com.megachollos.testUtils.integration;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

/**
 * Base class that starts a Docker Compose container
 */
@Slf4j
@RequiredArgsConstructor
public abstract class BaseDockerComposeExtension extends TestCallback {

  /**
   * Test container info: name, internal port and yml file
   */
  protected DockerComposeInfo dockerComposeInfo;

  /**
   * Test container. Managed by child class as is static
   */
  protected abstract DockerComposeContainer<?> getDockerContainer();

  /**
   * Container instantiation is child specific
   */
  protected abstract void instantiate(File file);

  /**
   * JUnit lifecycle methods
   */
  protected abstract void beforeAllTests(ExtensionContext junitContext);

  protected abstract void beforeEachTest(ExtensionContext junitContext);

  protected abstract void afterEachTest(ExtensionContext junitContext);

  protected abstract void afterAllTests(ExtensionContext junitContext);


  /**
   * Start docker compose before all tests
   */
  @Override
  public void beforeAll(ExtensionContext junitContext) {
    // Null instance -> instantiate
    if (getDockerContainer() == null) {
      File file = createTemporalFile();
      instantiate(file);
      file.deleteOnExit();
    }
    boolean isRunning = getDockerContainer()
        .getContainerByServiceName(dockerComposeInfo.getServiceName())
        .map(ContainerState::isRunning).orElse(false);

    // Stopped container -> start
    if (!isRunning) {
      getDockerContainer().withStartupTimeout(Duration.ofMinutes(15L)).start();

      Integer port = getDockerContainer().getServicePort(dockerComposeInfo.getServiceName(),
          dockerComposeInfo.getServicePort());
      log.info("Test Container {} started at port {}", dockerComposeInfo.getServiceName(), port);
      beforeAllTests(junitContext);
    }
  }

  @Override
  public void beforeEach(ExtensionContext junitContext) {
    beforeEachTest(junitContext);
  }

  @Override
  public void afterEach(ExtensionContext junitContext) {
    afterEachTest(junitContext);
  }


  @Override
  public void afterAll(ExtensionContext junitContext) {
    // By default do nohing. All containers will be running until all tests passes
    afterAllTests(junitContext);
  }

  // ----- COMMON

  /**
   * Retrieves the URI for accessing a service running inside a Docker Compose container.
   */
  protected String getUrl() {
    String serviceName = dockerComposeInfo.getServiceName();
    Integer servicePort = dockerComposeInfo.getServicePort();
    Assert.assertNotNull(serviceName);
    Assert.assertNotNull(servicePort);

    ContainerState containerState = getContainerState();
    Assert.assertTrue(containerState.isRunning());

    return getDockerContainer().getServiceHost(serviceName, servicePort) + ":"
        + getDockerContainer().getServicePort(serviceName, servicePort);
  }

  /**
   * Retrieves the state of the container associated with the current service.
   */
  protected ContainerState getContainerState() {
    String serviceName = dockerComposeInfo.getServiceName();
    return getDockerContainer()
        .getContainerByServiceName(serviceName)
        .orElseThrow(() -> new IllegalStateException("Service not found: " + serviceName));
  }

  /**
   * Creates a temporary file with the specified prefix and suffix
   */
  @SneakyThrows
  protected File createTemporalFile() {
    String yml = dockerComposeInfo.getYmlFile();
    File file;
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(yml)) {
      String tempFolderPath = System.getProperty("java.io.tmpdir");
      File tempFolder = new File(tempFolderPath, "testContainers");
      tempFolder.mkdir();

      file = File.createTempFile(dockerComposeInfo.getServiceName(), yml, tempFolder);
      String s = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
      FileUtils.writeLines(file, List.of(s));
    }

    return file;
  }

}