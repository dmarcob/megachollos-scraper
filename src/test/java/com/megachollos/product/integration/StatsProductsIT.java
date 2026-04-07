package com.megachollos.product.integration;

import static com.megachollos.testUtils.providers.BrandProvider.getBrandEntity;
import static com.megachollos.testUtils.providers.CategoryProvider.getCategoryEntity;
import static com.megachollos.testUtils.providers.ModelProvider.getModelEntity;
import static com.megachollos.testUtils.providers.ProductProvider.getProductDocument;
import static com.megachollos.testUtils.providers.SearchProvider.getFilter;
import static com.megachollos.testUtils.providers.StatsProvider.getStat;
import static com.megachollos.testUtils.providers.StatsProvider.getStatsCriteria;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.shared.infrastructure.rest.dtos.search.FilterOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsCriteria;
import com.megachollos.testUtils.bases.BaseIntegrationTest;
import io.restassured.common.mapper.TypeRef;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class StatsProductsIT extends BaseIntegrationTest {

  public static final String PATH = "/products/stats";

  public static Stream<Arguments> invalidStatsCriteriaProvider() {
    return Stream.of(
        // Missing stats
        Arguments.of(getStatsCriteria().withStats(null)),
        // Null stat operator
        Arguments.of(getStatsCriteria().withStats(List.of(getStat().withOperator(null)))),
        // Null stat field
        Arguments.of(getStatsCriteria().withStats(List.of(getStat().withField(null)))),
        // Null filter operator
        Arguments.of(getStatsCriteria().withFilters(List.of(getFilter().withOperator(null)))),
        // Null filter field
        Arguments.of(getStatsCriteria().withFilters(List.of(getFilter().withField(null))))
    );
  }

  private Long savedModelId;

  @BeforeEach
  void beforeEach() {
    jpaBrandRepository.save(getBrandEntity());
    categoryRepository.save(getCategoryEntity());
    savedModelId = modelRepository.save(getModelEntity()).getId();
  }

  @Test
  @SneakyThrows
  void stats_OK() {
    // GIVEN save product with reference 1
    elastic.withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .save(List.of(getProductDocument().withModel(savedModelId).withReference("1").withDiscount(BigDecimal.valueOf(1L))),
            IndexCoordinates.of(index));
    // AND save product with reference 2
    elastic.withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .save(List.of(getProductDocument().withModel(savedModelId).withReference("2")
            .withDiscount(BigDecimal.valueOf(2L))), IndexCoordinates.of(index));
    // AND save product with reference 3
    elastic.withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .save(List.of(getProductDocument().withModel(savedModelId).withReference("3")
            .withDiscount(BigDecimal.valueOf(3L))), IndexCoordinates.of(index));

    StatsCriteria searchCriteria = getStatsCriteria()
        .withFilters(List.of(getFilter().withField("reference").withOperator(
            FilterOperator.LESS_THAN).withValue("3")))
        .withStats(List.of(getStat().withField("discount").withOperator(StatOperator.STATS)));

    // WHEN get statitstics of field "discount" filtering by reference < 3
    Map<String, Object> result = given()
        .port(this.localServerPort)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(searchCriteria)
        .when()
        .post(PATH)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .as(new TypeRef<>() {
        });

    // THEN
    assertThat(result).isNotNull();
    List<Map<String, Object>> statsResults = (List) result.get("statResults");
    assertThat(statsResults).hasSize(1);
    assertThat(statsResults.get(0).get("field")).isEqualTo("discount");
    assertThat(statsResults.get(0).get("min")).isEqualTo(1.0);
    assertThat(statsResults.get(0).get("max")).isEqualTo(2.0);
    assertThat(statsResults.get(0).get("average")).isEqualTo(1.50);

  }

  @ParameterizedTest
  @MethodSource("invalidStatsCriteriaProvider")
  void stats_invalidStatsCriteria_badRequest(StatsCriteria criteria) {
    // GIVEN save product documents
    elastic.withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .save(List.of(getProductDocument().withModel(savedModelId)), IndexCoordinates.of(index));

    // WHEN, THEN
    given()
        .port(this.localServerPort)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(criteria)
        .when()
        .post(PATH)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }
}
