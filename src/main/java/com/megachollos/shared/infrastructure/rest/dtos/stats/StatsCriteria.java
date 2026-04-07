package com.megachollos.shared.infrastructure.rest.dtos.stats;

import com.megachollos.shared.infrastructure.rest.dtos.search.Filter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import lombok.extern.slf4j.Slf4j;

@Data
@Builder
@With
@Slf4j
public class StatsCriteria {

  @Valid
  private List<Filter> filters;

  @Valid
  @NotNull
  private List<Stat> stats;
}
