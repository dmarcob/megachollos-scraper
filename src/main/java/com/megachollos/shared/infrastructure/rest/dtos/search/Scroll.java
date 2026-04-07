package com.megachollos.shared.infrastructure.rest.dtos.search;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.With;

@Data
@With
@Builder(toBuilder = true)
public class Scroll {

  @Schema(description = "Identificador del batch de elementos a consultar")
  private String scrollId;

  @Schema(description = "Tamaño del batch de elementos a consultar")
  @NotNull
  @Min(1)
  private Integer scrollSize;

  @Schema(description = "Tiempo en milisegundos en el que se mantiene el contexto de scroll")
  @NotNull
  @Min(1)
  private Long scrollTimeInMillis;
}
