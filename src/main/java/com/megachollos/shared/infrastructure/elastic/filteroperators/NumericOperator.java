package com.megachollos.shared.infrastructure.elastic.filteroperators;

import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_FILTER_OPERATOR;
import static com.megachollos.shared.infrastructure.elastic.FieldType.DYNAMIC;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermRangeQuery;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import java.util.List;

public class NumericOperator extends BaseFilterOperator {

  @Override
  //Filter will check if field with name f.getField() is in the range specified by f.getValue()
  //Updates the mustQuery in all cases
  public void apply(List<Query> mustQuery, List<Query> mustNotQuery, Filter f) {
    Query query = super.duplicateValues(f.allValues(), (value) -> TermRangeQuery
        .of(builder -> {
              TermRangeQuery.Builder queryBuilder = builder.field(DYNAMIC.withIndex(f.getField()));
              return switch (f.getOperator()) {
                case GREATER_THAN -> queryBuilder.gt(value);
                case GREATER_THAN_OR_EQUAL_TO -> queryBuilder.gte(value);
                case LESS_THAN -> queryBuilder.lt(value);
                case LESS_THAN_OR_EQUAL_TO -> queryBuilder.lte(value);
                default -> throw new BadRequestException(UNSUPPORTED_FILTER_OPERATOR, f.getOperator());
              };
            }
        )
        ._toRangeQuery()._toQuery());

    mustQuery.add(query);
  }
}
