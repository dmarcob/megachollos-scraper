package com.megachollos.testUtils.providers;

import static com.megachollos.testUtils.providers.SearchProvider.getFilter;

import com.megachollos.shared.infrastructure.rest.dtos.stats.Stat;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatOperator;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsCriteria;
import com.megachollos.shared.infrastructure.rest.dtos.stats.StatsResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.GroupCountElementStatResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.GroupCountStatResult;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.StatResult;
import java.util.List;

public class StatsProvider {

  private final static String FIELD = "reference";
  private final static String VALUE = "value";

  public static StatsCriteria getStatsCriteria() {
    return StatsCriteria.builder()
        .filters(List.of(getFilter()))
        .stats(List.of(getStat()))
        .build();
  }

  public static Stat getStat() {
    return Stat.builder().field(FIELD).operator(StatOperator.STATS).build();
  }

  public static StatsResult getStatsResult() {
    return StatsResult.builder()
        .filters(List.of(getFilter()))
        .stats(List.of(getStat()))
        .statResults(List.of(getStatResult()))
        .build();
  }

  public static StatResult getStatResult() {
    return GroupCountStatResult.builder()
        .field(FIELD)
        .values(List.of(GroupCountElementStatResult.builder().value(VALUE).count(1L).build()))
        .build();
  }
}
