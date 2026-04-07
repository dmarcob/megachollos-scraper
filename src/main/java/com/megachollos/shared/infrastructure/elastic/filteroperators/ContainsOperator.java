package com.megachollos.shared.infrastructure.elastic.filteroperators;


import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_FILTER_OPERATOR;
import static com.megachollos.shared.infrastructure.elastic.FieldType.KEYWORD;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import java.util.List;

public class ContainsOperator extends BaseFilterOperator {

  @Override
  //Filter will check if field with name f.getField() (does not) contains the value f.getValue()
  //Updates the mustQuery if operator is CONTAINS, updates the mustNotQuery if operator is NOT_CONTAINS
  public void apply(List<Query> mustQuery, List<Query> mustNotQuery, Filter f) {
    Query query = super.duplicateValues(f.allValues(), (value) -> WildcardQuery
        .of(builder ->
            builder
                .caseInsensitive(true)
                // Filter over keyword indexed value
                .field(KEYWORD.withIndex(f.getField()))
                .value(switch (f.getOperator()) {
                  case CONTAINS, NOT_CONTAINS -> "*" + value + "*";
                  case STARTS_WITH -> value + "*";
                  case ENDS_WITH -> "*" + value;
                  default ->
                      throw new BadRequestException(UNSUPPORTED_FILTER_OPERATOR, f.getOperator());
                })
        )
        ._toQuery());

    switch (f.getOperator()) {
      case CONTAINS, STARTS_WITH, ENDS_WITH -> mustQuery.add(query);
      case NOT_CONTAINS -> mustNotQuery.add(query);
      default -> throw new BadRequestException(UNSUPPORTED_FILTER_OPERATOR, f.getOperator());
    }
  }
}
