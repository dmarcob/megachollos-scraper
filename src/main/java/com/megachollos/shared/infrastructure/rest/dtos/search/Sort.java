package com.megachollos.shared.infrastructure.rest.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class Sort {

  @Schema(
      description = "Campo sobre el que hacer la ordenación"
  )
  @NotNull
  private String field;

  @Schema(
      description = "Orden a aplicar al campo (ascendiente o descendiente)",
      example = "desc",
      defaultValue = "asc"
  )
  private SortOrder sort;

  public Sort(String field, SortOrder sort) {
    this.field = field;
    this.sort = sort != null ? sort : SortOrder.ASC;
  }

}
