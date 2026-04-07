package com.megachollos.shared.infrastructure.elastic.filteroperators;

import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_FILTER_OPERATOR;
import static com.megachollos.shared.infrastructure.elastic.FieldType.KEYWORD;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import java.util.List;

public class EqualityOperator extends BaseFilterOperator {

  @Override
  //Filter will check if field with name f.getField() has (not) exactly the value f.getValue()
  //Updates the mustQuery if operator is EQUALS, updates the mustNotQuery if operator is NOT_EQUALS
  public void apply(List<Query> mustQuery, List<Query> mustNotQuery, Filter f) {
    Query query;

    query = super.duplicateValues(f.allValues(), (value) -> TermQuery
        .of(builder ->
            builder
                .caseInsensitive(true)
                .field(KEYWORD.withIndex(f.getField()))
                .value(value)
        )
        ._toQuery());

    switch (f.getOperator()) {
      case EQUAL -> mustQuery.add(query);
      case NOT_EQUAL -> mustNotQuery.add(query);
      default -> throw new BadRequestException(UNSUPPORTED_FILTER_OPERATOR, f.getOperator());
    }
  }
}
