package com.megachollos.shared.infrastructure.elastic.filteroperators;

import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_FILTER_OPERATOR;

import co.elastic.clients.elasticsearch._types.query_dsl.ExistsQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import java.util.List;

public class NullityOperator extends BaseFilterOperator {

  @Override
  //Filter will check if field with name f.getField() is (not) null
  //Updates the mustQuery if operator is IS_NULL, updates the mustNotQuery if operator is IS_NOT_NULL
  public void apply(List<Query> mustQuery, List<Query> mustNotQuery, Filter f) {
    Query query = ExistsQuery
        .of(builder ->
            builder
                .field(f.getField())
        )
        ._toQuery();

    switch (f.getOperator()) {
      case IS_NULL -> mustNotQuery.add(query);
      case IS_NOT_NULL -> mustQuery.add(query);
      default -> throw new BadRequestException(UNSUPPORTED_FILTER_OPERATOR, f.getOperator());
    }
  }
}
