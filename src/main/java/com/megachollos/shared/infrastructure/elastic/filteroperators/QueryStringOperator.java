package com.megachollos.shared.infrastructure.elastic.filteroperators;

import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_FILTER_OPERATOR;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import java.util.List;

public class QueryStringOperator extends BaseFilterOperator {

  @Override
  public void apply(List<Query> mustQuery, List<Query> mustNotQuery, Filter f) {
    Query query;

    query = super.duplicateValues(f.allValues(), (value) -> QueryStringQuery
        .of(builder ->
            builder
                .query(f.getValue())
                .fields(List.of(f.getField()))
        )
        ._toQuery());

    switch (f.getOperator()) {
      case QUERY_STRING -> mustQuery.add(query);
      default -> throw new BadRequestException(UNSUPPORTED_FILTER_OPERATOR, f.getOperator());
    }
  }
}
