package com.megachollos.shared.infrastructure.elastic.statoperators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StatsAggregate;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.stats.Stat;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.MaxMinStatResult;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class StatsOperatorTest extends BaseUnitTest {

  private static final String FIELD = "field";

  @InjectMocks
  private StatsOperator operator;

  @Mock
  private Aggregate aggregate;

  @Mock
  private StatsAggregate statsAggregate;

  @Test
  void apply_validOperator_OK() {
    // GIVEN
    Stat stat = Stat.builder().field(FIELD).operator(StatOperator.STATS).build();

    // WHEN, THEN
    assertDoesNotThrow(() -> operator.apply(stat));
  }

  @Test
  void apply_invalidOperator_throwException() {
    // GIVEN
    Stat stat = Stat.builder().field(FIELD).operator(StatOperator.GROUPCOUNT).build();

    // WHEN, THEN
    assertThrows(BadRequestException.class, (() -> operator.apply(stat)));
  }

  @Test
  void toStatResult_OK() {
    // GIVEN an aggregate with min and max
    when(aggregate.stats()).thenReturn(statsAggregate);
    when(statsAggregate.min()).thenReturn(1.0000);
    when(statsAggregate.max()).thenReturn(2.0000);
    when(statsAggregate.avg()).thenReturn(1.5000);
    // AND expect a stat result with same min and max
    MaxMinStatResult expected = MaxMinStatResult.builder().field(FIELD)
        .min(BigDecimal.valueOf(1.0).setScale(2, RoundingMode.HALF_UP))
        .max(BigDecimal.valueOf(2.00).setScale(2, RoundingMode.HALF_UP))
        .average(BigDecimal.valueOf(1.50).setScale(2, RoundingMode.HALF_UP))
        .build();

    // WHEN
    MaxMinStatResult result = (MaxMinStatResult) operator.toStatResult(FIELD, aggregate);

    // THEN
    assertThat(result).isEqualTo(expected);
  }
}