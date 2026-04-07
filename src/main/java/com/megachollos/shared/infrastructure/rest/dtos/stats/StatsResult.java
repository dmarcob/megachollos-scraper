package com.megachollos.shared.infrastructure.rest.dtos.stats;

import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import com.megachollos.shared.infrastructure.rest.dtos.stats.statresult.StatResult;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@With
@Slf4j
public class StatsResult {

  private List<Filter> filters;
  private List<Stat> stats;
  private List<StatResult> statResults;
}
