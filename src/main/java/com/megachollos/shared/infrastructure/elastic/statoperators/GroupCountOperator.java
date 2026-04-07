package com.megachollos.shared.infrastructure.elastic.statoperators;


import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_STAT_OPERATOR;
import static com.megachollos.shared.infrastructure.elastic.FieldType.KEYWORD;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.stats.Stat;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.GroupCountElementStatResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.GroupCountStatResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.StatResult;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Group field by values and count total. Ej: { "field": "ecommerce", "values": [ { "value":
 * "keychron", "count": 21 } ] }
 */
public class GroupCountOperator extends BaseStatOperator {

  @Override
  public Aggregation apply(Stat stat) {
    if (!StatOperator.GROUPCOUNT.equals(stat.getOperator())) {
      throw new BadRequestException(UNSUPPORTED_STAT_OPERATOR, stat.getOperator());
    }

    return Aggregation.of(
        builder -> builder.terms(t -> t.field(KEYWORD.withIndex(stat.getField()))));
  }

  @Override
  public StatResult toStatResult(String field, Aggregate aggregate) {
    List<GroupCountElementStatResult> values = aggregate.sterms()
        .buckets().array().stream().map(bucket ->
            GroupCountElementStatResult.builder()
                .value(bucket.key().stringValue())
                .count(bucket.docCount())
                .build()
        ).collect(Collectors.toList());
    return GroupCountStatResult.builder().field(field).values(values).build();
  }
}
