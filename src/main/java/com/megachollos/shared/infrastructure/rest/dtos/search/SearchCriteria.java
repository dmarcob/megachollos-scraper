package com.megachollos.shared.infrastructure.rest.dtos.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder(toBuilder = true)
@With
@JsonInclude(Include.NON_NULL)
public class SearchCriteria {

  @Valid
  @NotNull
  private Scroll scroll;

  @Valid
  private List<Filter> filters;

  @Valid
  private List<Sort> sorts;
}
