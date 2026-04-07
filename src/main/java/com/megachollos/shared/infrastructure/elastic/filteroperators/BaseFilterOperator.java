package com.megachollos.shared.infrastructure.elastic.filteroperators;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import java.util.List;
import java.util.function.Function;

public abstract class BaseFilterOperator {
  
  public abstract void apply(List<Query> mustQuery, List<Query> mustNotQuery,
      Filter f);

  /**
   * Duplicates the values using the provided duplicator function.
   */
  protected final Query duplicateValues(List<String> values, Function<String, Query> duplicator) {
    List<Query> queries = values.stream().map(duplicator).toList();
    return BoolQuery.of(builder -> builder.minimumShouldMatch("1").should(queries))._toQuery();
  }
}