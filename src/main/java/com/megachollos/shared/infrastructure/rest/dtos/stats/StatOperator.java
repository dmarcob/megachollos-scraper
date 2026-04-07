package com.megachollos.shared.infrastructure.rest.dtos.stats;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatOperator {
  /**
   * Calcula el maximo, minimo y media
   */
  STATS("stats"),

  /**
   * Agrupa por valores y hace un conteo del total de cada grupo
   */
  GROUPCOUNT("groupCount");

  @Schema(description = "Operador para especificar el tipo de agregación estadistica", example = "maxMin")
  @JsonValue
  private final String operator;
}
