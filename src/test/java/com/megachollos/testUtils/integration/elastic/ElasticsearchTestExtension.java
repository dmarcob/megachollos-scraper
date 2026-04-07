package com.megachollos.testUtils.integration.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.megachollos.testUtils.integration.BaseDockerComposeExtension;
import com.megachollos.testUtils.integration.DockerComposeInfo;
import com.megachollos.testUtils.integration.TestProperties;
import java.io.File;
import java.io.IOException;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class ElasticsearchTestExtension extends BaseDockerComposeExtension {

  private static DockerComposeContainer<?> elasticSearchComposeContainer;

  ElasticsearchTestExtension() {
    super.dockerComposeInfo = DockerComposeInfo.builder()
        .serviceName("elasticsearch")
        .servicePort(9200)
        .ymlFile("docker-compose-elasticsearch.yml").build();
  }

  @Override
  protected DockerComposeContainer<?> getDockerContainer() {
    return elasticSearchComposeContainer;
  }

  @Override
  protected void instantiate(File file) {
    elasticSearchComposeContainer = new DockerComposeContainer<>(file)
        .withExposedService(dockerComposeInfo.getServiceName(), dockerComposeInfo.getServicePort())
        .waitingFor(dockerComposeInfo.getServiceName(), Wait.forLogMessage(".*started.*", 1));
  }

  @Override
  public void beforeAllTests(ExtensionContext junitContext) {
    TestProperties.setElasticProperties(getUrl());
  }

  @Override
  protected void beforeEachTest(ExtensionContext junitContext) {
    // Do nothing
  }

  @Override
  @SneakyThrows
  public void afterEachTest(ExtensionContext junitContext) {
    // Create extra client
    ElasticsearchClient client = createClient();

    // Drop all indexes
    client.indices().get(c -> c.index("*")).result().keySet().forEach(index -> {
      try {
        client.indices().delete(c -> c.index(index));
      } catch (IOException e) {
        // Do nothing
      }
    });
  }

  @Override
  protected void afterAllTests(ExtensionContext junitContext) {
    // Do nothing
  }

  private ElasticsearchClient createClient() {
    RestClient restClient = RestClient.builder(HttpHost.create(getUrl())).build();
    ElasticsearchTransport transport = new RestClientTransport(restClient,
        new JacksonJsonpMapper());
    return new ElasticsearchClient(transport);
  }
}