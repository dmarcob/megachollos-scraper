package com.megachollos.shared.infrastructure.elastic.builders;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.megachollos.shared.infrastructure.elastic.statoperators.BaseStatOperator;
import com.megachollos.shared.infrastructure.elastic.statoperators.GroupCountOperator;
import com.megachollos.shared.infrastructure.elastic.statoperators.StatsOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.Stat;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.StatResult;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class StatBuilder {

  private static final Map<StatOperator, BaseStatOperator> OPERATORS =
      new EnumMap<>(StatOperator.class);

  static {
    registerOperator(StatOperator.STATS, new StatsOperator());
    registerOperator(StatOperator.GROUPCOUNT, new GroupCountOperator());
  }

  public Aggregation toAggregation(Stat stat) {
    StatOperator operator = stat.getOperator();
    BaseStatOperator op = OPERATORS.get(operator);
    return op.apply(stat);
  }

  public List<StatResult> toStatResult(List<Stat> stats,
      Map<String, Aggregate> map) {
    List<StatResult> statResults = new ArrayList<>();
    for (Stat stat : stats) {
      StatOperator operator = stat.getOperator();
      BaseStatOperator op = OPERATORS.get(operator);
      if (Objects.nonNull(op)) {
        statResults.add(op.toStatResult(stat.getField(), map.get(stat.getField())));
      } else {
        log.warn("Could not apply operator {} as implementation is null", operator);
      }
    }
    return statResults;
  }

  private static void registerOperator(StatOperator statOperator, BaseStatOperator operator) {
    OPERATORS.put(statOperator, operator);
  }

}