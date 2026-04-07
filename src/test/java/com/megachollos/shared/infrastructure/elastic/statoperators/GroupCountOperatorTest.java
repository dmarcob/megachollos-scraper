package com.megachollos.shared.infrastructure.elastic.statoperators;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import com.megachollos.shared.domain.exceptions.BadRequestException;
import com.megachollos.shared.infrastructure.rest.dtos.stats.Stat;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.GroupCountElementStatResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.GroupCountStatResult;
import com.megachollos.testUtils.bases.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class GroupCountOperatorTest extends BaseUnitTest {

  private static final String FIELD = "field";
  private static final String VALUE = "value";

  @InjectMocks
  private GroupCountOperator operator;

  @Mock
  private Aggregate aggregate;

  @Mock
  private Buckets<StringTermsBucket> buckets;

  @Mock
  private StringTermsAggregate stringTermsAggregate;

  @Test
  void apply_validOperator_OK() {
    // GIVEN
    Stat stat = Stat.builder().field(FIELD).operator(StatOperator.GROUPCOUNT).build();

    // WHEN, THEN
    assertDoesNotThrow(() -> operator.apply(stat));
  }

  @Test
  void apply_invalidOperator_throwException() {
    // GIVEN
    Stat stat = Stat.builder().field(FIELD).operator(StatOperator.STATS).build();

    // WHEN, THEN
    assertThrows(BadRequestException.class, (() -> operator.apply(stat)));
  }

  @Test
  void toStatResult_OK() {
    // GIVEN an aggregation of "value": 1
    when(aggregate.sterms()).thenReturn(stringTermsAggregate);
    when(stringTermsAggregate.buckets()).thenReturn(buckets);
    when(buckets.array()).thenReturn(
        List.of(StringTermsBucket.of(b -> b.key(FieldValue.of(VALUE)).docCount(1))));
    // AND expect a stat result of "value": 1
    GroupCountStatResult expected = GroupCountStatResult.builder()
        .field(FIELD)
        .values(List.of(GroupCountElementStatResult.builder().value(VALUE).count(1L).build()))
        .build();

    // WHEN
    GroupCountStatResult result = (GroupCountStatResult) operator.toStatResult(FIELD, aggregate);

    // THEN
    assertThat(result).isEqualTo(expected);
  }
}