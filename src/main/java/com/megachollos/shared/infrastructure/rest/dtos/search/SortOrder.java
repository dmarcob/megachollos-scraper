package com.megachollos.shared.infrastructure.rest.dtos.search;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum SortOrder {
  ASC("asc"),
  DESC("desc");

  @JsonValue
  @Schema(description = "Valor del orden de una ordenación", example = "asc")
  private final String order;
}
