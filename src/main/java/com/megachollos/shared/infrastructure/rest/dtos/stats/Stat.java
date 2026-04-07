package com.megachollos.shared.infrastructure.rest.dtos.stats;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@Builder
@With
public class Stat {

  @Schema(description = "Nombre del campo sobre el que obtener la estadística")
  @NotNull
  @Size(min = 1)
  private String field;

  @Schema(description = "Tipo de estadística a obtener")
  @NotNull
  private StatOperator operator;
}
