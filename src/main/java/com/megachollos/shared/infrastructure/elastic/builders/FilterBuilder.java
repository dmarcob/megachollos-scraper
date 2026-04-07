package com.megachollos.shared.infrastructure.elastic.builders;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.megachollos.shared.infrastructure.elastic.filteroperators.BaseFilterOperator;
import com.megachollos.shared.infrastructure.elastic.filteroperators.ContainsOperator;
import com.megachollos.shared.infrastructure.elastic.filteroperators.EqualityOperator;
import com.megachollos.shared.infrastructure.elastic.filteroperators.NullityOperator;
import com.megachollos.shared.infrastructure.elastic.filteroperators.NumericOperator;
import com.megachollos.shared.infrastructure.elastic.filteroperators.QueryStringOperator;
import com.megachollos.shared.infrastructure.elastic.filteroperators.RegexpOperator;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import com.megachollos.shared.infrastructure.rest.dtos.search.FilterOperator;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@UtilityClass
@Slf4j
public class FilterBuilder {

  private static final Map<FilterOperator, BaseFilterOperator> OPERATORS =
      new EnumMap<>(FilterOperator.class);

  static {
    registerOperator(FilterOperator.EQUAL, new EqualityOperator());
    registerOperator(FilterOperator.NOT_EQUAL, new EqualityOperator());
    registerOperator(FilterOperator.CONTAINS, new ContainsOperator());
    registerOperator(FilterOperator.NOT_CONTAINS, new ContainsOperator());
    registerOperator(FilterOperator.STARTS_WITH, new ContainsOperator());
    registerOperator(FilterOperator.ENDS_WITH, new ContainsOperator());
    registerOperator(FilterOperator.IS_NULL, new NullityOperator());
    registerOperator(FilterOperator.IS_NOT_NULL, new NullityOperator());
    registerOperator(FilterOperator.GREATER_THAN, new NumericOperator());
    registerOperator(FilterOperator.GREATER_THAN_OR_EQUAL_TO, new NumericOperator());
    registerOperator(FilterOperator.LESS_THAN, new NumericOperator());
    registerOperator(FilterOperator.LESS_THAN_OR_EQUAL_TO, new NumericOperator());
    registerOperator(FilterOperator.REGEXP, new RegexpOperator());
    registerOperator(FilterOperator.IS_BLANK, new RegexpOperator());
    registerOperator(FilterOperator.IS_NOT_BLANK, new RegexpOperator());
    registerOperator(FilterOperator.QUERY_STRING, new QueryStringOperator());
  }

  public Query toQuery(List<Filter> filters) {
    if (CollectionUtils.isEmpty(filters)) {
      return BoolQuery.of(builder -> builder)._toQuery();
    }

    filters.forEach(Filter::validate);

    List<Query> mustQuery = new ArrayList<>();
    List<Query> mustNotQuery = new ArrayList<>();
    for (Filter filter : filters) {
      FilterOperator operator = filter.getOperator();
      BaseFilterOperator op = OPERATORS.get(operator);
      if (Objects.nonNull(op)) {
        op.apply(mustQuery, mustNotQuery, filter);
      } else {
        log.warn("Could not apply operator {} as implementation is null", operator);
      }
    }
    return BoolQuery.of(builder -> {
      if (!mustQuery.isEmpty()) {
        builder.must(mustQuery);
      }
      if (!mustNotQuery.isEmpty()) {
        builder.mustNot(mustNotQuery);
      }
      return builder;
    })._toQuery();
  }

  private static void registerOperator(FilterOperator filterOperator, BaseFilterOperator operator) {
    OPERATORS.put(filterOperator, operator);
  }

}