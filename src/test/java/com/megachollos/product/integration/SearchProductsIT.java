package com.megachollos.product.integration;

import static com.megachollos.testUtils.providers.BrandProvider.getBrandEntity;
import static com.megachollos.testUtils.providers.CategoryProvider.getCategoryEntity;
import static com.megachollos.testUtils.providers.ModelProvider.getModelEntity;
import static com.megachollos.testUtils.providers.ProductProvider.getProductDocument;
import static com.megachollos.testUtils.providers.SearchProvider.getFilter;
import static com.megachollos.testUtils.providers.SearchProvider.getScroll;
import static com.megachollos.testUtils.providers.SearchProvider.getSearchCriteria;
import static com.megachollos.testUtils.providers.SearchProvider.getSort;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.megachollos.product.infrastructure.rest.dtos.ProductDtoOut;
import com.megachollos.shared.infrastructure.rest.dtos.search.FilterOperator;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import com.megachollos.shared.infrastructure.rest.dtos.search.SortOrder;
import com.megachollos.testUtils.bases.BaseIntegrationTest;
import io.restassured.common.mapper.TypeRef;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import com.megachollos.model.jpa.entities.ModelEntity;
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

public class SearchProductsIT extends BaseIntegrationTest {

  public static final String PATH = "/products/search";

  public static Stream<Arguments> invalidSearchCriteriaProvider() {
    return Stream.of(
        // Missing scroll
        Arguments.of(getSearchCriteria().withScroll(null)),
        // Missing scroll size
        Arguments.of(getSearchCriteria().withScroll(getScroll().withScrollSize(null))),
        // Missing scroll time in millis
        Arguments.of(getSearchCriteria().withScroll(getScroll().withScrollTimeInMillis(null))),
        // Missing sort field
        Arguments.of(getSearchCriteria().withSorts(List.of(getSort().withField(null))))
    );
  }

  private Long savedModelId;

  @BeforeEach
  void beforeEach() {
    jpaBrandRepository.save(getBrandEntity());
    categoryRepository.save(getCategoryEntity());
    ModelEntity savedModel = modelRepository.save(getModelEntity());
    savedModelId = savedModel.getId();
  }

  @Test
  @SneakyThrows
  void search_OK() {
    // GIVEN save product with reference 1
    elastic.withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .save(List.of(getProductDocument().withModel(savedModelId).withReference("1").withDiscount(BigDecimal.valueOf(1L))),
            IndexCoordinates.of(index));
    // AND save product with reference 2
    elastic.withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .save(List.of(getProductDocument().withModel(savedModelId).withReference("2")
            .withDiscount(BigDecimal.valueOf(50L))), IndexCoordinates.of(index));
    // AND save product with reference 3
    elastic.withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .save(List.of(getProductDocument().withModel(savedModelId).withReference("3")
            .withDiscount(BigDecimal.valueOf(50L))), IndexCoordinates.of(index));

    SearchCriteria searchCriteria = getSearchCriteria()
        .withFilters(List.of(getFilter().withField("reference").withOperator(
            FilterOperator.LESS_THAN).withValue("3")))
        .withSorts(List.of(getSort().withField("reference").withSort(SortOrder.ASC)));

    // WHEN search filtering by reference < 3 and sorting by reference ASC
    SearchResult<ProductDtoOut> result = given()
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
    assertThat(result.getElements()).hasSize(2);
    assertThat(result.getElements().get(0).getReference()).isEqualTo("1");
    assertThat(result.getElements().get(1).getReference()).isEqualTo("2");
  }

  @ParameterizedTest
  @MethodSource("invalidSearchCriteriaProvider")
  void search_invalidSearchCriteria_badRequest(SearchCriteria searchCriteria) {
    // GIVEN save product documents
    elastic.withRefreshPolicy(RefreshPolicy.IMMEDIATE)
        .save(List.of(getProductDocument()), IndexCoordinates.of(index));

    // WHEN, THEN
    given()
        .port(this.localServerPort)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .body(searchCriteria)
        .when()
        .post(PATH)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value());
  }
}
