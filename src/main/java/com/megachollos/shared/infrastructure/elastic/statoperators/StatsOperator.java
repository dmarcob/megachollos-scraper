package com.megachollos.shared.infrastructure.elastic.statoperators;


import static com.megachollos.shared.domain.exceptions.errors.SharedError.UNSUPPORTED_STAT_OPERATOR;
import static com.megachollos.shared.infrastructure.elastic.FieldType.DYNAMIC;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.stats.Stat;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.MaxMinStatResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.StatResult;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Calculate min,max,average over a field. Ej: { "field": "price", "min": 59.5, "max": 105.3,
 * "average": 83.81 }
 */
public class StatsOperator extends BaseStatOperator {

  @Override
  public Aggregation apply(Stat stat) {
    if (!StatOperator.STATS.equals(stat.getOperator())) {
      throw new BadRequestException(UNSUPPORTED_STAT_OPERATOR, stat.getOperator());
    }

    return Aggregation.of(
        builder -> builder
            .stats(s -> s.field(DYNAMIC.withIndex(stat.getField()))));
  }

  @Override
  public StatResult toStatResult(String field, Aggregate aggregate) {
    double min = parseNullValue(aggregate.stats().min());
    double max = parseNullValue(aggregate.stats().max());
    double average = parseNullValue(aggregate.stats().avg());

    return MaxMinStatResult.builder()
        .field(field)
        .min(toBigDecimal(min))
        .max(toBigDecimal(max))
        .average(toBigDecimal(average))
        .build();
  }

  // Esto es un fix sobre la api de Spring, estan guardando min y max como primitivos
  // Sin embargo elastic puede devolver null.
  // Valuor esperado: min = null, max = null
  // Valor actual: min = infinity, max = -Infinity
  //
  // Eliminar este fix cuando lo corrigan en la api
  private double parseNullValue(double value) {
    if (value < 0 || value > 1000000000) {
      // Se ha detectado que el valor deberia ser null
      return 0;
    }
    return value;
  }

  private BigDecimal toBigDecimal(double value) {
    return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
  }
}
