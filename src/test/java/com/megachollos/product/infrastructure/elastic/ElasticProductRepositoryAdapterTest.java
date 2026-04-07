package com.megachollos.product.infrastructure.elastic;

import static com.megachollos.shared.infrastructure.elastic.errors.ElasticError.ELASTIC_NO_SUCH_INDEX;
import static com.megachollos.shared.infrastructure.elastic.errors.ElasticError.ELASTIC_UNAVAILABLE;
import static com.megachollos.shared.infrastructure.elastic.errors.ElasticError.INVALID_QUERY;
import static com.megachollos.testUtils.providers.BrandProvider.getBrand;
import static com.megachollos.testUtils.providers.ModelProvider.getModel;
import static com.megachollos.testUtils.providers.ProductProvider.getProduct;
import static com.megachollos.testUtils.providers.ProductProvider.getProductDocument;
import static com.megachollos.testUtils.providers.SearchProvider.getScroll;
import static com.megachollos.testUtils.providers.SearchProvider.getSearchCriteria;
import static com.megachollos.testUtils.providers.StatsProvider.getStatsCriteria;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.megachollos.brand.domain.BrandRepository;
import com.megachollos.model.domain.ModelRepository;
import com.megachollos.product.domain.Product;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.domain.exceptions.GenericException;
import com.megachollos.shared.domain.exceptions.UnavailableException;
import com.megachollos.shared.domain.exceptions.errors.GenericError;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsResult;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.ResourceFailureException;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.test.util.ReflectionTestUtils;

public class ElasticProductRepositoryAdapterTest extends BaseUnitTest {

  @InjectMocks
  private ElasticProductRepositoryAdapter repository;

  @Mock
  private ElasticsearchTemplate elastic;

  @Mock
  private ModelRepository modelRepository;

  @Mock
  private BrandRepository brandRepository;

  @Mock
  private SearchScrollHits<ProductDocument> searchScrollHits;

  @Mock
  private SearchHits<ProductDocument> searchHits;

  @Mock
  private SearchHit<ProductDocument> searchHit;

  public static Stream<Arguments> exceptionProvider() {
    return Stream.of(
        Arguments.of(
            NoSuchIndexException.class, UnavailableException.class, ELASTIC_NO_SUCH_INDEX),
        Arguments.of(
            UncategorizedElasticsearchException.class, BadRequestException.class,
            INVALID_QUERY),
        Arguments.of(
            ResourceFailureException.class, UnavailableException.class,
            ELASTIC_UNAVAILABLE)
    );
  }

  public static Stream<Arguments> saveArgumentProvider() {
    return Stream.of(
        // No conditions, it saves all elements
        Arguments.of(List.of(), 1),
        // Conditions always match, it saves all elements
        Arguments.of(List.of((Predicate<Product>) (product) -> true), 1),
        // Condition never match, it does not save any elements
        Arguments.of(List.of((Predicate<Product>) (product) -> false), 0)
    );
  }

  @BeforeEach
  public void beforeEach() {
    ReflectionTestUtils.setField(repository, "index", "index");
    lenient().when(modelRepository.findById(anyLong())).thenReturn(Optional.of(getModel()));
    lenient().when(brandRepository.findByUniqueName(anyString()))
        .thenReturn(Optional.of(getBrand()));
  }

  @ParameterizedTest(name = "{0}")
  @CsvSource({
      "true",
      "false"
  })
  void exists_OK(Boolean given) {
    // GIVEN
    when(elastic.exists(anyString(), any(IndexCoordinates.class))).thenReturn(given);

    // WHEN
    boolean result = repository.exists(getProduct());

    // THEN
    assertThat(result).isEqualTo(given);
  }

  @ParameterizedTest
  @MethodSource("exceptionProvider")
  void exists_throwException_throwException(Class<Exception> given,
      Class<GenericException> expectedException,
      GenericError expectedError) {
    // GIVEN
    when(elastic.exists(anyString(), any(IndexCoordinates.class))).thenThrow(given);

    // WHEN
    GenericException result = assertThrows(expectedException,
        () -> repository.exists(getProduct()));

    // THEN
    assertThat(result.getCode()).isEqualTo(expectedError.getCode());
  }

  @ParameterizedTest
  @MethodSource("saveArgumentProvider")
  void save_OK(List<Predicate<Product>> conditions, Integer expectedNumElementsSaved) {
    // WHEN
    repository.save(List.of(getProduct()), conditions);

    // THEN
    ArgumentCaptor<List<ProductDocument>> captor = ArgumentCaptor.forClass(List.class);
    verify(elastic).save(captor.capture(), any(IndexCoordinates.class));
    List<ProductDocument> capturedDocuments = captor.getValue();
    assertThat(capturedDocuments.size()).isEqualTo(expectedNumElementsSaved);
  }

  @ParameterizedTest
  @MethodSource("exceptionProvider")
  void save_throwException_throwException(Class<Exception> given,
      Class<GenericException> expectedException,
      GenericError expectedError) {
    // GIVEN
    when(elastic.save(any(), any(IndexCoordinates.class))).thenThrow(given);

    // WHEN
    GenericException result = assertThrows(expectedException,
        () -> repository.save(List.of(getProduct()), List.of()));

    // THEN
    assertThat(result.getCode()).isEqualTo(expectedError.getCode());
  }

  @Test
  void scroll_startScroll_OK() {
    // GIVEN a search criteria starting scroll
    SearchCriteria searchCriteria = getSearchCriteria().withScroll(
        getScroll().withScrollSize(1).withScrollId(null));
    // AND mock that scroll to returns one result
    when(elastic.searchScrollStart(anyLong(), any(), eq(ProductDocument.class), any())).thenReturn(
        searchScrollHits);
    when(searchScrollHits.get()).thenReturn(Stream.of(searchHit));
    when(searchScrollHits.getScrollId()).thenReturn("scrollId");
    when(searchHit.getContent()).thenReturn(getProductDocument());

    // WHEN
    SearchResult<Product> result = repository.scroll(searchCriteria);

    // THEN
    assertThat(result).isNotNull();
    assertThat(result.getScroll()).isNotNull();
    assertThat(result.getScroll().getScrollId()).isEqualTo("scrollId");
    assertThat(result.getScroll().getScrollTimeInMillis()).isEqualTo(
        searchCriteria.getScroll().getScrollTimeInMillis());
    assertThat(result.getScroll().getScrollSize()).isEqualTo(
        searchCriteria.getScroll().getScrollSize());
    assertThat(result.getElements()).hasSize(1);
    assertThat(result.getElements().get(0)).usingRecursiveComparison().isEqualTo(getProduct());
  }

  @Test
  void scroll_continueScroll_OK() {
    // GIVEN a search criteria starting scroll
    SearchCriteria searchCriteria = getSearchCriteria().withScroll(
        getScroll().withScrollSize(1).withScrollId("scrollId"));
    // AND mock scroll to not returns results
    when(elastic.searchScrollContinue(anyString(), anyLong(), eq(ProductDocument.class),
        any())).thenReturn(
        searchScrollHits);
    when(searchScrollHits.get()).thenReturn(Stream.of());

    // WHEN
    SearchResult<Product> result = repository.scroll(searchCriteria);

    // THEN
    assertThat(result).isNotNull();
    assertThat(result.getScroll()).isNotNull();
    assertThat(result.getScroll().getScrollId()).isNull();
    assertThat(result.getScroll().getScrollTimeInMillis()).isEqualTo(
        searchCriteria.getScroll().getScrollTimeInMillis());
    assertThat(result.getScroll().getScrollSize()).isEqualTo(
        searchCriteria.getScroll().getScrollSize());
    assertThat(result.getElements()).isEmpty();
  }

  @Test
  void stats_withoutStats_OK() {
    // GIVEN
    StatsCriteria statsCriteria = getStatsCriteria().withStats(List.of());
    when(elastic.search(any(NativeQuery.class), eq(ProductDocument.class), any()))
        .thenReturn(searchHits);
    when(searchHits.getAggregations()).thenReturn(null);

    // WHEN
    StatsResult result = repository.stats(statsCriteria);

    // THEN
    assertThat(result).isNotNull();
    assertThat(result.getFilters()).isEqualTo(statsCriteria.getFilters());
    assertThat(result.getStats()).isEmpty();
    assertThat(result.getStatResults()).isEmpty();
  }
}
