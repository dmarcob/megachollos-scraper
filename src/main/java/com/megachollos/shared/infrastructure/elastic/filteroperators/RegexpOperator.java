package com.megachollos.shared.infrastructure.elastic.filteroperators;

import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_FILTER_OPERATOR;
import static com.megachollos.shared.infrastructure.elastic.FieldType.KEYWORD;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RegexpQuery;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import java.util.List;

public class RegexpOperator extends BaseFilterOperator {

  @Override
  //Filter will check if field with name f.getField() matches the regular expression f.getValue()
  public void apply(List<Query> mustQuery, List<Query> mustNotQuery, Filter f) {

    Query query = switch (f.getOperator()) {
      case REGEXP -> super.duplicateValues(f.allValues(), value -> buildQuery(f, value));
      case IS_BLANK, IS_NOT_BLANK -> buildQuery(f, "[^ ]+");
      default -> throw new BadRequestException(UNSUPPORTED_FILTER_OPERATOR, f.getOperator());
    };

    switch (f.getOperator()) {
      case REGEXP, IS_NOT_BLANK -> mustQuery.add(query);
      case IS_BLANK -> mustNotQuery.add(query);
      default -> throw new BadRequestException(UNSUPPORTED_FILTER_OPERATOR, f.getOperator());
    }
  }

  private Query buildQuery(Filter f, String value) {
    return RegexpQuery
        .of(builder ->
            builder
                .caseInsensitive(true)
                // Filter over keyword indexed value
                .field(KEYWORD.withIndex(f.getField()))
                .value(value)
        )
        ._toQuery();
  }
}
