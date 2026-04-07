package com.megachollos.testUtils.bases;

import com.megachollos.brand.infrastructure.jpa.repositories.JpaBrandRepository;
import com.megachollos.category.jpa.repositories.JpaCategoryRepository;
import com.megachollos.model.jpa.repositories.JpaModelRepository;
import com.megachollos.testUtils.integration.elastic.annotations.EnableElasticsearchTests;
import com.megachollos.testUtils.integration.postgres.EnablePostgresTests;
import com.megachollos.testUtils.providers.ObjectProvider;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@EnablePostgresTests
@EnableElasticsearchTests
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public abstract class BaseIntegrationTest {

  @LocalServerPort
  protected int localServerPort;

  @Value("${megachollos.elasticsearch.index}")
  protected String index;

  @Autowired
  protected JpaCategoryRepository categoryRepository;

  @Autowired
  protected JpaBrandRepository jpaBrandRepository;

  @Autowired
  protected JpaModelRepository modelRepository;

  @Autowired
  protected ElasticsearchTemplate elastic;

  @BeforeEach
  void beforeEach() {
    // Create index with INMEDIATE refresh
    IndexOperations indexOperations = elastic
        .withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .indexOps(IndexCoordinates.of(index));
    // AND given mapping
    Document document = Document.from(ObjectProvider.fromClassPath("json/mappings.json"));
    // AND given settings
    Map<String, Object> settings = Document.from(
        ObjectProvider.fromClassPath("json/settings.json"));
    indexOperations.create(settings, document);
  }
}
