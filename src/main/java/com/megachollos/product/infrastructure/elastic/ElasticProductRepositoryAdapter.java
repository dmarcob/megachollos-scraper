package com.megachollos.product.infrastructure.elastic;

import static com.megachollos.shared.infrastructure.elastic.errors.ElasticError.ELASTIC_NO_SUCH_INDEX;
import static com.megachollos.shared.infrastructure.elastic.errors.ElasticError.ELASTIC_UNAVAILABLE;
import static com.megachollos.shared.infrastructure.elastic.errors.ElasticError.INVALID_QUERY;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.SingleBucketAggregateBase;
import com.megachollos.brand.domain.BrandRepository;
import com.megachollos.model.domain.ModelRepository;
import com.megachollos.product.domain.Product;
import com.megachollos.product.domain.ProductRepository;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.domain.exceptions.UnavailableException;
import com.megachollos.shared.infrastructure.elastic.builders.FilterBuilder;
import com.megachollos.shared.infrastructure.elastic.builders.SortBuilder;
import com.megachollos.shared.infrastructure.elastic.builders.StatBuilder;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import com.megachollos.shared.infrastructure.rest.dtos.search.Scroll;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.search.SearchResult;
import com.megachollos.shared.infrastructure.rest.dtos.search.Sort;
import com.megachollos.shared.infrastructure.rest.dtos.stats.Stat;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.StatResult;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchScrollHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticProductRepositoryAdapter implements ProductRepository {

  private final ElasticsearchTemplate elastic;
  private final ModelRepository modelRepository;
  private final BrandRepository brandRepository;

  @Value("${megachollos.elasticsearch.index}")
  private String index;

  @Override
  public boolean exists(Product product) {
    ProductDocument document = ProductDocument.fromDomain(product);
    return executeOperation(
        () -> elastic.exists(document.getElasticsearchId(), IndexCoordinates.of(index)), index);
  }

  @Override
  public void save(List<Product> products) {
    save(products, List.of());
  }

  @Override
  public void save(List<Product> products, List<Predicate<Product>> conditions) {
    // Map to documents
    List<ProductDocument> documents = products.stream()
        .filter(product -> conditions.stream().allMatch(condition -> condition.test(product)))
        .peek(product -> {
          // Set audit dates
          product.setCreatedAt(LocalDateTime.now());
        })
        .map(ProductDocument::fromDomain)
        .toList();

    log.debug("Saving {} products in elastic", documents.size());
    executeOperation(() -> elastic.save(documents, IndexCoordinates.of(index)), index);
  }

  @Override
  public SearchResult<Product> scroll(SearchCriteria criteria) {
    String scrollId = criteria.getScroll().getScrollId();
    Long scrollTime = criteria.getScroll().getScrollTimeInMillis();
    Integer scrollSize = criteria.getScroll().getScrollSize();
    List<Filter> filters = criteria.getFilters();
    List<Sort> sorts = criteria.getSorts();

    // Create query with scroll
    NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder()
        .withPageable(PageRequest.of(0, scrollSize));

    // AND add filters
    nativeQueryBuilder.withQuery(FilterBuilder.toQuery(filters));

    // AND add sorts
    if (CollectionUtils.isNotEmpty(sorts)) {
      nativeQueryBuilder = nativeQueryBuilder.withSort(SortBuilder.of(sorts).build());
    }

    NativeQuery nativeQuery = nativeQueryBuilder
        // Count total elements even if are >= 10.000 (default count limit)
        .withTrackTotalHits(true)
        .build();
    SearchScrollHits<ProductDocument> hits;

    if (criteria.getScroll().getScrollId() == null) {
      // Cuando se inicia el scroll se indica la query
      hits = executeOperation(
          () -> elastic.searchScrollStart(scrollTime, nativeQuery,
              ProductDocument.class,
              IndexCoordinates.of(index)), index);
    } else {
      // Cuando se continua el scroll se indica el scroll id
      hits = executeOperation(
          () -> elastic.searchScrollContinue(scrollId, scrollTime, ProductDocument.class,
              IndexCoordinates.of(index)),
          index);
    }

    List<Product> products = hits.get()
        .map(SearchHit::getContent)
        .map(p -> p.toDomain(modelRepository, brandRepository))
        .toList();

    return SearchResult.<Product>builder()
        .scroll(Scroll.builder()
            .scrollId(scrollSize == products.size() ? hits.getScrollId() : null)
            .scrollTimeInMillis(scrollTime)
            .scrollSize(scrollSize)
            .build())
        .totalElements(hits.getTotalHits())
        .elements(products)
        .build();
  }

  @Override
  public StatsResult stats(StatsCriteria criteria) {

    // Create query
    NativeQueryBuilder nativeQueryBuiler = NativeQuery.builder().withMaxResults(0);

    // And add aggregations
    Aggregation aggregations = Aggregation.of(builder -> builder
        // Apply the filter to the results based on the provided criteria
        .filter(FilterBuilder.toQuery(criteria.getFilters()))
        // Perform separate aggregations for each stat
        .aggregations(criteria.getStats().stream()
            .collect(Collectors.toMap(Stat::getField, StatBuilder::toAggregation))
        )
    );
    nativeQueryBuiler.withAggregation("aggregations", aggregations);

    SearchHits<ProductDocument> hits = executeOperation(
        () -> elastic.search(nativeQueryBuiler.build(),
            ProductDocument.class,
            IndexCoordinates.of(index)), index);

    Map<String, Aggregate> map = Optional.ofNullable(
            (ElasticsearchAggregations) hits.getAggregations())
        .map(ElasticsearchAggregations::aggregationsAsMap)
        .map(a -> a.get("aggregations"))
        .map(ElasticsearchAggregation::aggregation)
        .map(org.springframework.data.elasticsearch.client.elc.Aggregation::getAggregate)
        .map(Aggregate::filter)
        .map(SingleBucketAggregateBase::aggregations)
        .orElseGet(HashMap::new);

    List<StatResult> statResults = StatBuilder.toStatResult(criteria.getStats(), map);

    return StatsResult.builder()
        .filters(criteria.getFilters())
        .stats(criteria.getStats())
        .statResults(statResults)
        .build();
  }

  private <T> T executeOperation(
      Supplier<T> operation, String index) {
    try {
      return operation.get();
    } catch (NoSuchIndexException e) {
      log.error("El índice {} no existe", index);
      throw new UnavailableException(ELASTIC_NO_SUCH_INDEX, index);
    } catch (UncategorizedElasticsearchException e) {
      log.warn("Elasticsearch ha respondido con un status {}", e.getStatusCode(), e);
      throw new BadRequestException(INVALID_QUERY, e.getResponseBody());
    } catch (Exception e) {
      log.error("Elasticsearch error", e);
      throw new UnavailableException(ELASTIC_UNAVAILABLE);
    }
  }
}